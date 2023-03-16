package pl.tiad.task1.backend.stoptype;

public class AccuracyStop implements StopType {

    private final double number;

    public AccuracyStop(double number) {
        this.number = number;
    }

    @Override
    public Double getNumber() {
        return number;
    }
}

