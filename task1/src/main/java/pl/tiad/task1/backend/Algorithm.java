package pl.tiad.task1.backend;

import pl.tiad.task1.backend.utils.IterationStop;
import pl.tiad.task1.backend.utils.StopType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.DoublePredicate;
import java.util.stream.IntStream;

public abstract class Algorithm {
    protected int dimensions;
    protected StopType stopType;
    protected double globalBestAdaptation = Double.MAX_VALUE;
    protected final List<Double> globalBest = new ArrayList<>();
    protected final List<Integer> iterations = new ArrayList<>();
    protected final List<Double> avgPopulationValues = new ArrayList<>();
    protected final List<Double> minPopulationValues = new ArrayList<>();

    private final DoublePredicate function = value -> {
        if (stopType instanceof IterationStop) {
            return value < stopType.getNumber();
        }
        return value > stopType.getNumber();
    };

    public Map<String, Double> start() {
        IntStream.range(0, dimensions).forEach(i -> globalBest.add(-1.0));

        int i = 0;
        do {
            algorithmStep(i);
            i++;
        } while (stopType instanceof IterationStop ? function.test(i) : function.test(globalBestAdaptation));

        Map<String, Double> bestResults = new HashMap<>();
        IntStream.range(0, dimensions)
                .forEach(index -> bestResults.put("X" + index, globalBest.get(index)));
        bestResults.put("Adaptation", globalBestAdaptation);
        return bestResults;
    }

    protected abstract void algorithmStep(int i);

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
