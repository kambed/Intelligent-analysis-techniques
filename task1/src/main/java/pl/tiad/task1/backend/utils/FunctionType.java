package pl.tiad.task1.backend.utils;

import java.util.List;
import java.util.function.Function;

public enum FunctionType {
    SPHERE(FunctionEvaluator::calculate);
    private final Function<List<Double>, Double> function;

    FunctionType(Function<List<Double>, Double> function) {
        this.function = function;
    }

    public Function<List<Double>, Double> getFunction() {
        return function;
    }
}
