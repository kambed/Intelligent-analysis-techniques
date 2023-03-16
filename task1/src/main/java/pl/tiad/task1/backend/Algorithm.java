package pl.tiad.task1.backend;

import pl.tiad.task1.backend.utils.AccuracyStop;
import pl.tiad.task1.backend.utils.IterationStop;
import pl.tiad.task1.backend.utils.StopType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public abstract class Algorithm {
    protected int dimensions;
    protected StopType stopType;
    protected double globalBestAdaptation = Double.MAX_VALUE;
    protected final List<Double> globalBest = new ArrayList<>();

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

    protected abstract void algorithmStep(int i);
}
