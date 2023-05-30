package pl.tiad.task1.backend.cfa;

import pl.tiad.task1.backend.utils.FunctionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public abstract class CuttlefishCell {

    protected final List<Double> pos = new ArrayList<>();
    protected final double maxX;
    protected final double minX;
    protected final FunctionType functionType;
    protected final int dimensions;
    protected final double r1;
    protected final double r2;
    protected final double v1;
    protected final double v2;
    protected final Random r = new Random();
    protected CuttlefishCell bestCell;

    protected CuttlefishCell(FunctionType functionType, double maxX, double minX, int dimensions,
                          double r1, double r2, double v1, double v2) {
        this.maxX = maxX;
        this.minX = minX;
        this.functionType = functionType;
        this.dimensions = dimensions;
        this.r1 = r1;
        this.r2 = r2;
        this.v1 = v1;
        this.v2 = v2;
        IntStream.range(0, dimensions).forEach(
                i -> pos.add(r.nextDouble(maxX - minX) + minX)
        );
    }

    public void move() {
        moveGroup();
        IntStream.range(0, dimensions).forEach(
                i -> {
                    if (pos.get(i) > maxX) {
                        pos.set(i, maxX);
                    }
                    if (pos.get(i) < minX) {
                        pos.set(i, minX);
                    }
                }
        );
    }

    public abstract void moveGroup();

    public List<Double> getPos() {
        return pos;
    }

    public void setPos(List<Double> reflections, List<Double> visibilities) {
        setPos(IntStream.range(0, dimensions).mapToDouble(i -> reflections.get(i) + visibilities.get(i)).boxed().toList());
    }

    public void setPos(List<Double> newPos) {
        if (calculateNewAdaptation(newPos) < calculateNewAdaptation(pos)) {
            IntStream.range(0, dimensions).forEach(
                    i -> pos.set(i, newPos.get(i))
            );
        }
    }

    public double calculateAdaptation() {
        return functionType.getFunction().apply(pos);
    }

    public double calculateNewAdaptation(List<Double> newPos) {
        return functionType.getFunction().apply(newPos);
    }

    public CuttlefishCell getBestCell() {
        return bestCell;
    }

    public void setBestCell(CuttlefishCell bestCell) {
        this.bestCell = bestCell;
    }

    public double getR() {
        return r.nextDouble(1) * (r1 - r2) + r2;
    }

    public double getV() {
        return r.nextDouble(1) * (v1 - v2) + v2;
    }

    public double getAV() {
        return pos.stream().mapToDouble(Double::doubleValue).average().orElseThrow();
    }


}
