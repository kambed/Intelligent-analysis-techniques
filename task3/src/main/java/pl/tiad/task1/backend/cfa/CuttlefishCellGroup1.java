package pl.tiad.task1.backend.cfa;

import pl.tiad.task1.backend.utils.FunctionType;

import java.util.List;
import java.util.stream.IntStream;

public class CuttlefishCellGroup1 extends CuttlefishCell {
    protected CuttlefishCellGroup1(FunctionType functionType, double maxX, double minX, int dimensions, double r1, double r2, double v1, double v2) {
        super(functionType, maxX, minX, dimensions, r1, r2, v1, v2);
    }

    @Override
    public void moveGroup() {
        List<Double> reflections = pos.stream().map(pos -> pos * getR()).toList();
        List<Double> visibilities = IntStream.range(0, dimensions).mapToDouble(i -> (bestCell.getPos().get(i) - pos.get(i)) * getV()).boxed().toList();
        IntStream.range(0, dimensions).forEach(
                i -> pos.set(i, reflections.get(i) + visibilities.get(i))
        );
    }
}
