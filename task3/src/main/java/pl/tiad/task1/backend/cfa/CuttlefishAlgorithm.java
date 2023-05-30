package pl.tiad.task1.backend.cfa;

import pl.tiad.task1.backend.Algorithm;
import pl.tiad.task1.backend.utils.FunctionType;
import pl.tiad.task1.backend.utils.StopType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class CuttlefishAlgorithm extends Algorithm {

    private final int numOfParticles;
    private final List<CuttlefishCell> cells = new ArrayList<>();
    private CuttlefishCell bestCell = null;

    public CuttlefishAlgorithm(StopType stopType, FunctionType functionType, int numOfParticles, double maxX,
                               double minX, int dimensions, double r1, double r2, double v1, double v2) {
        this.stopType = stopType;
        this.dimensions = dimensions;
        this.numOfParticles = numOfParticles;

        IntStream.range(0, numOfParticles).forEach(i -> {
                    CuttlefishCell c;
                    switch (i % 4) {
                        case 0 -> c = new CuttlefishCellGroup1(functionType, maxX, minX, dimensions, r1, r2, v1, v2);
                        case 1 -> c = new CuttlefishCellGroup2(functionType, maxX, minX, dimensions, r1, r2, v1, v2);
                        case 2 -> c = new CuttlefishCellGroup3(functionType, maxX, minX, dimensions, r1, r2, v1, v2);
                        case 3 -> c = new CuttlefishCellGroup4(functionType, maxX, minX, dimensions, r1, r2, v1, v2);
                        default -> throw new IllegalStateException("Unexpected value: " + i % 4);
                    }
                    cells.add(c);
                }
        );
        CuttlefishCell bestCell = cells.stream().min(Comparator.comparingDouble(CuttlefishCell::calculateAdaptation)).orElseThrow();
        for (CuttlefishCell c : cells) {
            c.setBestCell(bestCell);
        }
    }

    @Override
    protected void algorithmStep(int i) {
        for (CuttlefishCell c : cells) {
            c.move();
        }
        CuttlefishCell newBestCell = cells.stream().min(Comparator.comparingDouble(CuttlefishCell::calculateAdaptation)).orElseThrow();
        if (bestCell == null || newBestCell.calculateAdaptation() < bestCell.calculateAdaptation()) {
            bestCell = newBestCell;
            for (CuttlefishCell c : cells) {
                c.setBestCell(bestCell);
            }
            globalBestAdaptation = bestCell.calculateAdaptation();
            IntStream.range(0, dimensions).forEach(index -> globalBest.set(index, bestCell.getPos().get(index)));
        }
        double avgAdaptation = cells.stream().mapToDouble(CuttlefishCell::calculateAdaptation).average().orElseThrow();

        iterations.add(i + 1);
        minPopulationValues.add(bestCell.calculateAdaptation());
        avgPopulationValues.add(avgAdaptation);
    }
}
