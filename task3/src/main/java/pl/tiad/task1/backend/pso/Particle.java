package pl.tiad.task1.backend.pso;

import pl.tiad.task1.backend.utils.FunctionType;

import java.util.*;
import java.util.stream.IntStream;

public class Particle {
    private final List<Double> pos = new ArrayList<>();
    private final List<Double> speed = new ArrayList<>();
    private double adaptation;
    private final List<Double> bestPos = new ArrayList<>();
    private final List<Double> bestPosInSwarm = new ArrayList<>();
    private final List<Double> template = new ArrayList<>();
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
                    template.add(pos.get(i));
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
        double templateAcceleration = ((cognition + social) / 2) * r.nextDouble(1);
        IntStream.range(0, dimensions).forEach(
                i -> {
                    double inertionPart = inertion * speed.get(i);
                    double templatePart = templateAcceleration * (template.get(i) - pos.get(i));
                    speed.set(i, inertionPart + templatePart);
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

    public double getTemplateAdaptation() {
        return functionType.getFunction().apply(template);
    }

    public double getTemplateAdaptation(List<Double> newTemplate) {
        return functionType.getFunction().apply(newTemplate);
    }

    public double getPos(int index) {
        return pos.get(index);
    }

    public double getBestPos(int index) {
        return bestPos.get(index);
    }

    public double getBestPosInSwarm(int index) {
        return bestPosInSwarm.get(index);
    }

    public void setBestXInSwarm(int index, double bestXInSwarm) {
        this.bestPosInSwarm.set(index, bestXInSwarm);
    }

    public void setTemplate(int index, double template) {
        this.template.set(index, template);
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

    public double getMaxX() {
        return maxX;
    }

    public double getMinX() {
        return minX;
    }
}
