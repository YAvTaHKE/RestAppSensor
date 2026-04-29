package ru.moerti.springprojects.RestAppSensor.util;

public class SensorNotRegistrationException extends RuntimeException{

    public SensorNotRegistrationException(String message) {
        super(message);
    }
}
