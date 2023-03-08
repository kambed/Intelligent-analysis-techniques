package pl.tiad.task1.backend;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Particle {
    private double x;
    private double y;
    private double speedX;
    private double speedY;
    private double adaptation;
    private String function;
    private double bestX;
    private double bestY;
    private double bestXInSwarm;
    private double bestYInSwarm;
    private double bestAdaptation = Double.MAX_VALUE;
    private final double maxX;
    private final double maxY;
    private final double minX;
    private final double minY;
    private final double inertion;
    private final double cognition;
    private final double social;

    public Particle(double maxX, double maxY, double minX, double minY, String function,
                    double inertion, double cognition, double social) {
        this.maxX = maxX;
        this.maxY = maxY;
        this.minX = minX;
        this.minY = minY;
        Random r = new Random();
        this.x = r.nextDouble(maxX - minX) + minX;
        this.y = r.nextDouble(maxY - minY) + minY;
        this.speedX = 0;
        this.speedY = 0;
        this.adaptation = 0;
        this.function = function;
        calculateAdaptation();
        this.inertion = inertion;
        this.cognition = cognition;
        this.social = social;
    }

    public double calculateAdaptation() {
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", x);
        variables.put("y", y);
        adaptation = FunctionEvaluator.calculate(function, variables);
        if (adaptation < bestAdaptation) {
            bestAdaptation = adaptation;
            bestX = x;
            bestY = y;
        }
        return adaptation;
    }

    public void move() {
        double inertionPartX = inertion * speedX;
        double inertionPartY = inertion * speedY;
        Random r = new Random();
        double cognitionAcceleration = cognition * r.nextDouble(1);
        double cognitionPartX = cognitionAcceleration * (bestX - x);
        double cognitionPartY = cognitionAcceleration * (bestY - y);

        double socialAcceleration = social * r.nextDouble(1);
        double socialPartX = socialAcceleration * (bestXInSwarm - x);
        double socialPartY = socialAcceleration * (bestYInSwarm - y);

        speedX = inertionPartX + cognitionPartX + socialPartX;
        speedY = inertionPartY + cognitionPartY + socialPartY;

        x = x + speedX;
        if (x > maxX) {
            x = maxX;
        }
        if (x < minX) {
            x = minX;
        }
        y = y + speedY;
        if (y > maxY) {
            y = maxY;
        }
        if (y < minY) {
            y = minY;
        }
        calculateAdaptation();
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getSpeedX() {
        return speedX;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    public double getAdaptation() {
        return adaptation;
    }

    public void setAdaptation(double adaptation) {
        this.adaptation = adaptation;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public double getBestX() {
        return bestX;
    }

    public void setBestX(double bestX) {
        this.bestX = bestX;
    }

    public double getBestY() {
        return bestY;
    }

    public void setBestY(double bestY) {
        this.bestY = bestY;
    }

    public double getBestXInSwarm() {
        return bestXInSwarm;
    }

    public void setBestXInSwarm(double bestXInSwarm) {
        this.bestXInSwarm = bestXInSwarm;
    }

    public double getBestYInSwarm() {
        return bestYInSwarm;
    }

    public void setBestYInSwarm(double bestYInSwarm) {
        this.bestYInSwarm = bestYInSwarm;
    }

    public double getBestAdaptation() {
        return bestAdaptation;
    }

    public void setBestAdaptation(double bestAdaptation) {
        this.bestAdaptation = bestAdaptation;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
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
