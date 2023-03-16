package pl.tiad.task1.backend.pso;

import pl.tiad.task1.backend.stoptype.AccuracyStop;
import pl.tiad.task1.backend.stoptype.IterationStop;
import pl.tiad.task1.backend.stoptype.StopType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class ParticleSwarmAlgorithm {
    private final int numOfParticles;
    private final StopType stopType;
    private final List<Particle> particles = new ArrayList<>();
    private final List<Integer> iterations = new ArrayList<>();
    private final List<Double> avgPopulationValues = new ArrayList<>();
    private final List<Double> minPopulationValues = new ArrayList<>();
    private final int dimensions;

    private double globalBestAdaptation = Double.MAX_VALUE;
    private final List<Double> globalBest = new ArrayList<>();

    public ParticleSwarmAlgorithm(StopType stopType, int numOfParticles, double maxX, double minX, int dimensions,
                                  double inertion, double cognition, double social) {
        this.numOfParticles = numOfParticles;
        this.stopType = stopType;
        this.dimensions = dimensions;
        double bestAdaptation = Double.MAX_VALUE;
        Particle bestParticle = null;
        for (int i = 0; i < numOfParticles; i++) {
            Particle p = new Particle(maxX, minX, dimensions, inertion, cognition, social);
            particles.add(p);
            if (p.getAdaptation() < bestAdaptation) {
                bestAdaptation = p.getAdaptation();
                bestParticle = p;
            }
        }
        for (Particle p : particles) {
            Particle finalBestParticle = bestParticle;
            IntStream.range(0, dimensions)
                    .forEach(i -> p.setBestXInSwarm(i, finalBestParticle.getPos(i)));
        }
    }

    public Map<String, Double> start() {
        IntStream.range(0, dimensions).forEach(i -> globalBest.add(-1.0));
        if (stopType instanceof IterationStop) {
            for (int i = 0; i < stopType.getNumber(); i++) {
                algorithmStep(i);
            }
        } else if (stopType instanceof AccuracyStop) {
            int i = 0;
            do {
                algorithmStep(i);
                i++;
            } while (globalBestAdaptation > stopType.getNumber());
        }
        Map<String, Double> bestResults = new HashMap<>();
        IntStream.range(0, dimensions)
                .forEach(index -> bestResults.put("X" + index, globalBest.get(index)));
        bestResults.put("Adaptation", globalBestAdaptation);
        return bestResults;
    }

    public void algorithmStep(int i) {
        double bestAdaptation = Double.MAX_VALUE;
        Particle bestParticle = null;
        double avgAdaptation = 0;
        for (Particle p : particles) {
            p.move();
            if (p.getAdaptation() < bestAdaptation) {
                bestAdaptation = p.getAdaptation();
                bestParticle = p;
            }
            if (p.getAdaptation() < globalBestAdaptation) {
                globalBestAdaptation = p.getAdaptation();
                IntStream.range(0, dimensions)
                        .forEach(index -> globalBest.set(index, p.getPos(index)));
            }
            avgAdaptation += p.getAdaptation();
        }
        for (Particle p : particles) {
            Particle finalBestParticle = bestParticle;
            IntStream.range(0, dimensions)
                    .forEach(index -> p.setBestXInSwarm(index, finalBestParticle.getPos(index)));
        }
        iterations.add(i + 1);
        minPopulationValues.add(bestAdaptation);
        avgPopulationValues.add(avgAdaptation / numOfParticles);
    }

    public List<Integer> getIterations() {
        return iterations;
    }

    public List<Double> getAvgPopulationValues() {
        return avgPopulationValues;
    }

    public List<Double> getMinPopulationValues() {
        return minPopulationValues;
    }
}
