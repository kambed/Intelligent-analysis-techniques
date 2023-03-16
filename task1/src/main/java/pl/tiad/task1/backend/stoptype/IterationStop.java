package pl.tiad.task1.backend.stoptype;

public class IterationStop implements StopType {

    private final int number;

    public IterationStop(int number) {
        this.number = number;
    }

    @Override
    public Double getNumber() {
        return (double) number;
    }
}
