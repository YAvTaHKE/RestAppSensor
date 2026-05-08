package ru.moerti.springprojects.RestAppSensor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.moerti.springprojects.RestAppSensor.entity.Measure;
import ru.moerti.springprojects.RestAppSensor.repositories.MeasureRepository;
import ru.moerti.springprojects.RestAppSensor.repositories.SensorRepository;
import ru.moerti.springprojects.RestAppSensor.util.SensorNotFoundException;

@Service
@Transactional(readOnly = true)
public class MeasureService {

    private final MeasureRepository measureRepository;
    private final SensorRepository sensorRepository;

    @Autowired
    public MeasureService(MeasureRepository measureRepository, SensorRepository sensorRepository) {
        this.measureRepository = measureRepository;
        this.sensorRepository = sensorRepository;
    }

    @Transactional
    public Measure save(Measure measure) {
        if (measure == null) {
            throw new IllegalStateException("Measure cannot be null");
        }
        if (!sensorRepository.existsByName(measure.getSensor().getName())) {
            throw new SensorNotFoundException(
                    "Sensor with name '" + measure.getSensor().getName() + "' not found!"
            );
        }

        return measureRepository.save(measure);
    }
}
