package ru.moerti.springprojects.RestAppSensor.util;

public class SensorNotFoundException extends RuntimeException{

    public SensorNotFoundException(String message) {
        super(message);
    }
}
