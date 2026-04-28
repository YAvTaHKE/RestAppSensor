package ru.moerti.springprojects.RestAppSensor.entity;

import jakarta.persistence.Entity;

@Entity
public class Measure {

    private String value;

    private boolean raining;

    private Sensor sensor;
}
