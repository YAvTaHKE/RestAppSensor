package ru.moerti.springprojects.RestAppSensor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.moerti.springprojects.RestAppSensor.repositories.MeasureRepository;

@Service
public class MeasureService {

    private final MeasureRepository measureRepository;

    @Autowired
    public MeasureService(MeasureRepository measureRepository) {
        this.measureRepository = measureRepository;
    }
}
