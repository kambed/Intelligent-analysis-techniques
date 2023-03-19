package pl.tiad.task1.backend.de;

import pl.tiad.task1.backend.Algorithm;
import pl.tiad.task1.backend.utils.FunctionType;
import pl.tiad.task1.backend.utils.StopType;

import java.util.*;
import java.util.stream.IntStream;

public class DifferentialEvolutionAlgorithm extends Algorithm {
    private final List<Individual> individuals = new ArrayList<>();
    private final FunctionType functionType;
    private final Random r = new Random();

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
}
