package pl.tiad.task1.backend.de;

import pl.tiad.task1.backend.pso.Particle;
import pl.tiad.task1.backend.utils.AccuracyStop;
import pl.tiad.task1.backend.utils.FunctionType;
import pl.tiad.task1.backend.utils.IterationStop;
import pl.tiad.task1.backend.utils.StopType;

import java.util.*;
import java.util.stream.IntStream;

public class DifferentialEvolutionAlgorithm {
    private final List<Individual> individuals = new ArrayList<>();
    private final StopType stopType;
    private final List<Integer> iterations = new ArrayList<>();
    private final List<Double> avgPopulationValues = new ArrayList<>();
    private final List<Double> minPopulationValues = new ArrayList<>();
    private final int dimensions;
    private final FunctionType functionType;
    private final Random r = new Random();

    private double globalBestAdaptation = Double.MAX_VALUE;
    private final List<Double> globalBest = new ArrayList<>();
    public DifferentialEvolutionAlgorithm(StopType stopType, FunctionType functionType, int numOfIndividuals, double maxX,
                                  double minX, int dimensions, double amplificationFactor, double crossChance) {
        this.stopType = stopType;
        this.dimensions = dimensions;
        this.functionType = functionType;
        IntStream.range(0, numOfIndividuals)
                .forEach(index -> individuals.add(
                        new Individual(functionType, maxX, minX, dimensions, amplificationFactor, crossChance)
                ));
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
        double avgAdaptation = 0;
        IntStream.range(0, individuals.size())
                .forEach(index -> {
                    Individual ind = individuals.get(index);
                    int ind1;
                    int ind2;
                    do {
                        ind1 = r.nextInt(0, individuals.size() - 1);
                        ind2 = r.nextInt(0, individuals.size() - 1);
                    } while (ind1 == ind2 || ind1 == index || ind2 == index);
                    ind.mutate(individuals.get(ind1), individuals.get(ind2));
                    ind.cross();
                    ind.selection();
                });
        for (Individual ind : individuals) {
            double adaptation = functionType.getFunction().apply(ind.getPos());
            if (globalBestAdaptation > adaptation) {
                globalBestAdaptation = adaptation;
                IntStream.range(0, dimensions)
                        .forEach(index -> globalBest.set(index, ind.getPos().get(index)));
            }
            if (bestAdaptation > adaptation) {
                bestAdaptation = adaptation;
            }
            avgAdaptation += adaptation;
        }
        avgAdaptation /= individuals.size();
        iterations.add(i + 1);
        minPopulationValues.add(bestAdaptation);
        avgPopulationValues.add(avgAdaptation);
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
