package pl.tiad.task2.backend.epso;

import pl.tiad.task2.backend.Algorithm;
import pl.tiad.task2.backend.pso.Particle;
import pl.tiad.task2.backend.pso.ParticleSwarmAlgorithm;
import pl.tiad.task2.backend.utils.StopType;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class EliteParticleSwarmAlgorithm extends Algorithm {

    private final List<ParticleSwarmAlgorithm> psoSwarms;
    private final Map<Integer, Particle> eliteParticles = new HashMap<>();
    private final Map<Integer, Double> psoBestValuesOfSwarms = new HashMap<>();
    private final Map<Integer, Double> psoAvgValuesOfSwarms = new HashMap<>();
    private final int numOfParticlesInEachSwarm;
    private final Random random = new Random();

    public EliteParticleSwarmAlgorithm(List<ParticleSwarmAlgorithm> psoSwarms, StopType stopType, int dimensions,
                                       int numOfParticlesInEachSwarm) {
        this.dimensions = dimensions;
        this.stopType = stopType;
        this.psoSwarms = psoSwarms;
        this.numOfParticlesInEachSwarm = numOfParticlesInEachSwarm;
    }

    @Override
    protected void algorithmStep(int i) {
        AtomicInteger swarmNum = new AtomicInteger(0);
        psoSwarms.forEach(
                psoSwarm -> eliteParticles.put(swarmNum.getAndIncrement(), psoSwarm.removeBestParticle())
        );
        eliteParticles.values().forEach(
                eliteParticle -> eliteParticle.setPos(calculateNewEliteParticlePos())
        );
        swarmNum.set(0);
        psoSwarms.forEach(
                psoSwarm -> {
                    Particle bestInSwarm = eliteParticles.get(swarmNum.getAndIncrement());
                    Map<String, Double> results = psoSwarm.algorithmStep();
                    psoSwarm.addParticle(bestInSwarm);
                    double bestAdaptation = Math.max(results.get("bestAdaptation"), bestInSwarm.getAdaptation());
                    psoBestValuesOfSwarms.put(swarmNum.get(), bestAdaptation);
                    psoAvgValuesOfSwarms.put(swarmNum.get(), (results.get("avgAdaptation") * numOfParticlesInEachSwarm +
                            bestInSwarm.getAdaptation()) / numOfParticlesInEachSwarm);
                    if (bestAdaptation < globalBestAdaptation) {
                        globalBestAdaptation = bestAdaptation;
                        IntStream.range(0, dimensions)
                                .forEach(index -> globalBest.set(index, psoSwarm.getBestParticle().getPos(index)));
                    }
                }
        );

        iterations.add(i + 1);
        minPopulationValues.add(psoBestValuesOfSwarms.values().stream().min(Double::compareTo).orElseThrow());
        avgPopulationValues.add(psoAvgValuesOfSwarms.values().stream().mapToDouble(Double::doubleValue).average().orElseThrow());
    }

    private List<Double> calculateNewEliteParticlePos() {
        List<Double> newPos = new ArrayList<>();
        IntStream.range(0, dimensions)
                .forEach(index -> newPos.add(eliteParticles.values().stream()
                        .mapToDouble(particle -> particle.getPos(index))
                        .average()
                        .orElseThrow() * (1 + random.nextGaussian(0, 1))));
        return newPos;
    }
}
