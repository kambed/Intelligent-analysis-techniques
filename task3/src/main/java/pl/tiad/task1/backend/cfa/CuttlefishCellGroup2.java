package pl.tiad.task1.backend.cfa;

import pl.tiad.task1.backend.utils.FunctionType;

import java.util.List;
import java.util.stream.IntStream;

public class CuttlefishCellGroup2 extends CuttlefishCell {
    protected CuttlefishCellGroup2(FunctionType functionType, double maxX, double minX, int dimensions, double r1, double r2, double v1, double v2) {
        super(functionType, maxX, minX, dimensions, r1, r2, v1, v2);
    }

    @Override
    public void moveGroup() {
        List<Double> visibilities = IntStream.range(0, dimensions).mapToDouble(i -> (bestCell.getPos().get(i) - pos.get(i)) * getV()).boxed().toList();
        List<Double> reflections = bestCell.getPos().stream().map(pos -> pos * getR()).toList();
        IntStream.range(0, dimensions).forEach(
                i -> pos.set(i, reflections.get(i) + visibilities.get(i))
        );
    }
}
