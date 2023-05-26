package pl.tiad.task1.backend.utils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public enum FunctionType {
    SPHERE(
            (List<Double> positions) -> positions.stream()
                    .mapToDouble(FunctionType::pow2)
                    .sum()
    ),
    F2(
            (List<Double> positions) -> IntStream.range(0, positions.size())
                    .mapToDouble(i -> pow2(positions.get(i) - (i + 1)))
                    .sum()
    ),
    ROSENBROCK(
            (List<Double> positions) -> IntStream.range(0, positions.size() - 1)
                    .mapToDouble(i -> 100 * pow2(positions.get(i + 1) - pow2(positions.get(i))) + pow2(positions.get(i) - 1))
                    .sum()
    ),
    GRINEWANK(
            (List<Double> positions) -> 0.00025 * positions.stream()
                    .mapToDouble(FunctionType::pow2)
                    .sum() - IntStream.range(0, positions.size())
                    .mapToDouble(i -> Math.cos(positions.get(i) / Math.sqrt(i + 1.0)))
                    .reduce(1, (a, b) -> a * b) + 1
    ),
    RASTRIGIN(
            (List<Double> positions) -> positions.stream()
                    .mapToDouble(pos -> pow2(pos) - 10 * Math.cos(2 * Math.PI * pos) + 10)
                    .sum()
    ),
    ACKLEY(
            (List<Double> positions) -> -20 * Math.exp(
                    -0.2 * Math.sqrt(
                            positions.stream()
                                    .mapToDouble(FunctionType::pow2)
                                    .sum() / positions.size()
                    )
            ) - Math.exp(
                    positions.stream()
                            .mapToDouble(pos -> Math.cos(2 * Math.PI * pos))
                            .sum() / positions.size()
            ) + 20 + Math.E
    );
    private final Function<List<Double>, Double> function;

    FunctionType(Function<List<Double>, Double> function) {
        this.function = function;
    }

    public Function<List<Double>, Double> getFunction() {
        return function;
    }

    private static double pow2(double x) {
        return x * x;
    }
}
