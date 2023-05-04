package pl.tiad.task2.backend.pso;

import pl.tiad.task2.backend.utils.FunctionType;

import java.util.*;
import java.util.stream.IntStream;

public class Particle {
    private final List<Double> pos = new ArrayList<>();
    private final List<Double> speed = new ArrayList<>();
    private double adaptation;
    private final List<Double> bestPos = new ArrayList<>();
    private final List<Double> bestPosInSwarm = new ArrayList<>();
    private double bestAdaptation = Double.MAX_VALUE;
    private final double maxX;
    private final double minX;
    private final double inertion;
    private final double cognition;
    private final double social;
    private final int dimensions;
    private final FunctionType functionType;
    private final Random r = new Random();

    public Particle(FunctionType functionType, double maxX, double minX, int dimensions,
                    double inertion, double cognition, double social) {
        this.functionType = functionType;
        this.maxX = maxX;
        this.minX = minX;
        IntStream.range(0, dimensions).forEach(
                i -> {
                    pos.add(r.nextDouble(maxX - minX) + minX);
                    speed.add(0.0);
                    bestPos.add(pos.get(i));
                    bestPosInSwarm.add(pos.get(i));
                }
        );
        this.dimensions = dimensions;
        this.adaptation = 0;
        calculateAdaptation();
        this.inertion = inertion;
        this.cognition = cognition;
        this.social = social;
    }

    public double calculateAdaptation() {
        adaptation = functionType.getFunction().apply(pos);
        if (adaptation < bestAdaptation) {
            bestAdaptation = adaptation;
            IntStream.range(0, dimensions)
                    .forEach(i -> bestPos.set(i, pos.get(i)));
        }
        return adaptation;
    }

    public void move() {
        double cognitionAcceleration = cognition * r.nextDouble(1);
        double socialAcceleration = social * r.nextDouble(1);
        IntStream.range(0, dimensions).forEach(
                i -> {
                    double inertionPart = inertion * speed.get(i);
                    double cognitionPart = cognitionAcceleration * (bestPos.get(i) - pos.get(i));
                    double socialPart = socialAcceleration * (bestPosInSwarm.get(i) - pos.get(i));
                    speed.set(i, inertionPart + cognitionPart + socialPart);
                }
        );
        IntStream.range(0, dimensions).forEach(
                i -> {
                    pos.set(i, pos.get(i) + speed.get(i));
                    if (pos.get(i) > maxX) {
                        pos.set(i, maxX);
                    }
                    if (pos.get(i) < minX) {
                        pos.set(i, minX);
                    }
                }
        );
        calculateAdaptation();
    }

    public double getAdaptation() {
        return adaptation;
    }

    public double getPos(int index) {
        return pos.get(index);
    }

    public void setBestXInSwarm(int index, double bestXInSwarm) {
        this.bestPosInSwarm.set(index, bestXInSwarm);
    }

    public double getInertion() {
        return inertion;
    }

    public double getCognition() {
        return cognition;
    }

    public double getSocial() {
        return social;
    }
}
