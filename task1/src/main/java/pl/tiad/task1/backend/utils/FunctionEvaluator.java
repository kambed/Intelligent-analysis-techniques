package pl.tiad.task1.backend.utils;

import java.util.List;

public class FunctionEvaluator {

    private FunctionEvaluator() {
    }

    public static Double calculate(List<Double> positions) {
        return positions.stream()
                .mapToDouble(pos -> pos * pos)
                .sum();
    }
}
