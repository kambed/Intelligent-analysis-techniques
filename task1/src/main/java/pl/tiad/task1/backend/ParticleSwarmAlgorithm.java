package pl.tiad.task1.backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class ParticleSwarmAlgorithm {
    private final int numOfParticles;
    private final int numOfIterations;
    private final List<Particle> particles = new ArrayList<>();
    private final List<Integer> iterations = new ArrayList<>();
    private final List<Double> avgPopulationValues = new ArrayList<>();
    private final List<Double> minPopulationValues = new ArrayList<>();
    private final int dimensions;

    public ParticleSwarmAlgorithm(int numOfIterations, int numOfParticles, double maxX, double minX, int dimensions,
                                  double inertion, double cognition, double social) {
        this.numOfParticles = numOfParticles;
        this.numOfIterations = numOfIterations;
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
        double globalBestAdaptation = Double.MAX_VALUE;
        List<Double> globalBest = new ArrayList<>();
        IntStream.range(0, dimensions).forEach(i -> globalBest.add(-1.0));
        for (int i = 0; i < numOfIterations; i++) {
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
                            .forEach(index -> {
                                globalBest.set(index, p.getPos(index));
                            });
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
        Map<String, Double> bestResults = new HashMap<>();
        IntStream.range(0, dimensions)
                .forEach(index -> bestResults.put("X" + index, globalBest.get(index)));
        bestResults.put("Adaptation", globalBestAdaptation);
        return bestResults;
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
