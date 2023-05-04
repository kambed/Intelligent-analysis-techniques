package pl.tiad.task2.backend.opso;

import javafx.util.Pair;
import pl.tiad.task2.backend.Algorithm;
import pl.tiad.task2.backend.pso.ParticleSwarmAlgorithm;
import pl.tiad.task2.backend.utils.StopType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class OsmosisParticlaSwarmAlgorithm extends Algorithm {

    private final List<ParticleSwarmAlgorithm> psoSwarms;
    private final int migrationInterval;
    private final Map<Integer, Double> psoBestValuesOfSwarms = new HashMap<>();
    private final Map<Integer, Double> psoAvgValuesOfSwarms = new HashMap<>();
    private final List<Pair<Integer, Integer>> migrationPairs;
    public OsmosisParticlaSwarmAlgorithm(List<ParticleSwarmAlgorithm> psoSwarms, StopType stopType, int migrationInterval, int dimensions) {
        this.dimensions = dimensions;
        this.stopType = stopType;
        this.psoSwarms = psoSwarms;
        this.migrationInterval = migrationInterval;
        this.migrationPairs = new ArrayList<>();
        IntStream.range(0, psoSwarms.size() - 1).forEach(
                swarm -> migrationPairs.add(new Pair<>(swarm, swarm + 1))
        );
        migrationPairs.add(new Pair<>(psoSwarms.size() - 1, 0));
    }

    @Override
    protected void algorithmStep(int i) {
        AtomicInteger swarmNum = new AtomicInteger(0);
        psoSwarms.forEach(
                psoSwarm -> {
                    Map<String, Double> results = psoSwarm.algorithmStep();
                    double bestAdaptation = results.get("bestAdaptation");
                    psoBestValuesOfSwarms.put(swarmNum.getAndIncrement(), bestAdaptation);
                    psoAvgValuesOfSwarms.put(swarmNum.get(), results.get("avgAdaptation"));
                    if (bestAdaptation < globalBestAdaptation) {
                        globalBestAdaptation = bestAdaptation;
                        IntStream.range(0, dimensions)
                                .forEach(index -> globalBest.set(index, psoSwarm.getBestParticle().getPos(index)));
                    }
                }
        );

        if (i != 0 && i % migrationInterval == 0) {
            migrationPairs.forEach(
                    migrationPair -> {
                        Pair<Integer, Integer> direction = getMigrationDirection(migrationPair);
                        psoSwarms.get(direction.getKey()).replaceWorst(
                                psoSwarms.get(direction.getValue()).getBestParticles(
                                        calculateLambda(psoBestValuesOfSwarms.get(migrationPair.getKey()), psoBestValuesOfSwarms.get(migrationPair.getValue()))
                                )
                        );
                    }
            );
        }

        iterations.add(i + 1);
        minPopulationValues.add(psoBestValuesOfSwarms.values().stream().min(Double::compareTo).orElseThrow());
        avgPopulationValues.add(psoAvgValuesOfSwarms.values().stream().mapToDouble(Double::doubleValue).average().orElseThrow());
    }

    private Pair<Integer, Integer> getMigrationDirection(Pair<Integer, Integer> migrationPair) {
        if (psoBestValuesOfSwarms.get(migrationPair.getKey()) < psoBestValuesOfSwarms.get(migrationPair.getValue())) {
            return migrationPair;
        }
        return new Pair<>(migrationPair.getValue(), migrationPair.getKey());
    }

    private double calculateLambda(double swarm1Fitness, double swarm2Fitness) {
        return Math.abs(swarm1Fitness - swarm2Fitness) / Math.max(swarm1Fitness, swarm2Fitness);
    }
}
