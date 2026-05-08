package ru.moerti.springprojects.RestAppSensor.util;

public class MeasureInvalidData extends RuntimeException {
    public MeasureInvalidData(String message) {
        super(message);
    }
}
