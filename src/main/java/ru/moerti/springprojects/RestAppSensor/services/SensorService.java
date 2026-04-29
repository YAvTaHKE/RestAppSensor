package ru.moerti.springprojects.RestAppSensor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.moerti.springprojects.RestAppSensor.controllers.SensorController;

@Service
public class SensorService {

    private SensorController sensorController;

    @Autowired
    public SensorService(SensorController sensorController) {
        this.sensorController = sensorController;
    }
}
