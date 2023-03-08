package pl.tiad.task1.backend;

import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.StaticVariableSet;

import java.util.Map;

public class FunctionEvaluator {
    //(x+2*y-7)^2 + (2*x+y-5)^2
    //0.26 * (x^2+y^2) - 0.48 * x * y
    //(x^2+y-11)^2 + (x+y^2-7)^2
    public static double calculate(String function, Map<String, Double> variables) {
        DoubleEvaluator eval = new DoubleEvaluator();
        StaticVariableSet<Double> vars = new StaticVariableSet<>();
        variables.forEach(vars::set);
        return eval.evaluate(function, vars);
    }
}
