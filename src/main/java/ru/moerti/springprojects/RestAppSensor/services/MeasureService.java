package ru.moerti.springprojects.RestAppSensor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.moerti.springprojects.RestAppSensor.controllers.MeasurmentController;

@Service
public class MeasureService {

    private MeasurmentController measurmentController;

    @Autowired
    public MeasureService(MeasurmentController measurmentController) {
        this.measurmentController = measurmentController;
    }
}
