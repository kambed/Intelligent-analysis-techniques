package pl.tiad.task1.backend;

import java.util.List;

public class FunctionEvaluator {
    public static double calculate(List<Double> positions) {
        return positions.stream()
                .mapToDouble(pos -> pos * pos)
                .sum();
    }
}
