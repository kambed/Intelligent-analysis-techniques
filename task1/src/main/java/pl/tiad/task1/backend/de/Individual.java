package pl.tiad.task1.backend.de;

import pl.tiad.task1.backend.utils.FunctionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class Individual {
    private final List<Double> pos = new ArrayList<>();
    private final List<Double> posMutant = new ArrayList<>();
    private final List<Double> posTrial = new ArrayList<>();
    private final double amplificationFactor;
    private final double crossChance;
    private final FunctionType functionType;
    private final Random r = new Random();

    public Individual(FunctionType functionType, double maxX, double minX, int dimensions,
                      double amplificationFactor, double crossChance) {
        this.amplificationFactor = amplificationFactor;
        this.crossChance = crossChance;
        this.functionType = functionType;
        IntStream.range(0, dimensions).forEach(
                i -> {
                    pos.add(r.nextDouble(maxX - minX) + minX);
                    posMutant.add(0.0);
                    posTrial.add(0.0);
                }
        );
    }

    public List<Double> getPos() {
        return pos;
    }

    public void mutate(Individual i1, Individual i2) {
        IntStream.range(0, pos.size())
                .forEach(index ->
                        posMutant.set(index, pos.get(index) + (amplificationFactor * (i1.pos.get(index) - i2.pos.get(index))))
                );
    }

    public void cross() {
        IntStream.range(0, pos.size())
                .forEach(index -> {
                    double trialPosX = pos.get(index);
                    if (r.nextDouble() < crossChance) {
                        trialPosX = posMutant.get(index);
                    }
                    posTrial.set(index, trialPosX);
                });
    }

    public void selection() {
        if (functionType.getFunction().apply(pos) > functionType.getFunction().apply(posMutant)) {
            IntStream.range(0, pos.size())
                    .forEach(index -> pos.set(index, posMutant.get(index)));
        }
    }
}
