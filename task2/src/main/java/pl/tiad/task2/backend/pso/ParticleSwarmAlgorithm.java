package pl.tiad.task2.backend.pso;

import pl.tiad.task2.backend.utils.FunctionType;

import java.util.*;
import java.util.stream.IntStream;

public class ParticleSwarmAlgorithm {
    private final int numOfParticles;
    private final int dimensions;
    private Particle bestParticle = null;
    private final List<Particle> particles = new ArrayList<>();

    public ParticleSwarmAlgorithm(FunctionType functionType, int numOfParticles, double maxX,
                                  double minX, int dimensions, double inertion, double cognition, double social) {
        this.numOfParticles = numOfParticles;
        this.dimensions = dimensions;
        double bestAdaptation = Double.MAX_VALUE;
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

    public Map<String, Double> algorithmStep() {
        double bestAdaptation = Double.MAX_VALUE;
        double avgAdaptation = 0;
        for (Particle p : particles) {
            p.move();
            if (p.getAdaptation() < bestAdaptation) {
                bestAdaptation = p.getAdaptation();
                bestParticle = p;
            }
            avgAdaptation += p.getAdaptation();
        }
        for (Particle p : particles) {
            Particle finalBestParticle = bestParticle;
            IntStream.range(0, dimensions)
                    .forEach(index -> p.setBestXInSwarm(index, finalBestParticle.getPos(index)));
        }
        Map<String, Double> results = new HashMap<>();
        results.put("bestAdaptation", bestAdaptation);
        results.put("avgAdaptation", avgAdaptation / numOfParticles);
        return results;
    }

    public Particle getBestParticle() {
        return bestParticle;
    }

    public Particle removeBestParticle() {
        Particle best = getBestParticle();
        particles.remove(best);
        return best;
    }

    public void addParticle(Particle particle) {
        particles.add(particle);
        if (particle.getAdaptation() < bestParticle.getAdaptation()) {
            bestParticle = particle;
            for (Particle p : particles) {
                Particle finalBestParticle = bestParticle;
                IntStream.range(0, dimensions)
                        .forEach(index -> p.setBestXInSwarm(index, finalBestParticle.getPos(index)));
            }
        }
    }

    public List<Particle> getBestParticles(double percentage) {
        return particles.stream()
                .sorted(Comparator.comparingDouble(Particle::getAdaptation))
                .limit((int) (numOfParticles * percentage))
                .toList();
    }

    public void replaceWorst(List<Particle> particles) {
        this.particles.sort(Comparator.comparingDouble(Particle::getAdaptation));
        for (int i = this.particles.size() - 1; i > this.particles.size() - 1 - particles.size(); i--) {
            this.particles.set(i, particles.get(particles.size() - this.particles.size() + i));
        }
    }
}
