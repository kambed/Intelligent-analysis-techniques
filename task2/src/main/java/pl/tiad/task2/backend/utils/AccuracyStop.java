package pl.tiad.task2.backend.utils;

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

