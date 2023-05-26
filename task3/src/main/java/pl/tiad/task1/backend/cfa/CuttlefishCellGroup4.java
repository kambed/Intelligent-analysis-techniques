package pl.tiad.task1.backend.cfa;

import pl.tiad.task1.backend.utils.FunctionType;

import java.util.stream.IntStream;

public class CuttlefishCellGroup4 extends CuttlefishCell {
    protected CuttlefishCellGroup4(FunctionType functionType, double maxX, double minX, int dimensions, double r1, double r2, double v1, double v2) {
        super(functionType, maxX, minX, dimensions, r1, r2, v1, v2);
    }

    @Override
    public void moveGroup() {
        double newMaxX = Math.min(bestCell.getAV() + ((maxX - minX) / 4), maxX);
        double newMinX = Math.max(bestCell.getAV() - ((maxX - minX) / 4), minX);
        IntStream.range(0, dimensions).forEach(
                i -> pos.set(i, r.nextDouble(newMaxX - newMinX) + newMinX)
        );
    }
}
