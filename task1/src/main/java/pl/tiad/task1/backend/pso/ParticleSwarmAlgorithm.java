package pl.tiad.task1.backend.pso;

import pl.tiad.task1.backend.Algorithm;
import pl.tiad.task1.backend.utils.FunctionType;
import pl.tiad.task1.backend.utils.StopType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ParticleSwarmAlgorithm extends Algorithm {
    private final int numOfParticles;
    private final List<Particle> particles = new ArrayList<>();

    public ParticleSwarmAlgorithm(StopType stopType, FunctionType functionType, int numOfParticles, double maxX,
                                  double minX, int dimensions, double inertion, double cognition, double social) {
        this.numOfParticles = numOfParticles;
        this.stopType = stopType;
        this.dimensions = dimensions;
        double bestAdaptation = Double.MAX_VALUE;
        Particle bestParticle = null;
        for (int i = 0; i < numOfParticles; i++) {
            Particle p = new Particle(functionType, maxX, minX, dimensions, inertion, cognition, social);
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
}
