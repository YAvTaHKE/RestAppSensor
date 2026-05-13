package ru.moerti.springprojects.RestAppSensor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.moerti.springprojects.RestAppSensor.entity.Measure;
import ru.moerti.springprojects.RestAppSensor.repositories.MeasureRepository;
import ru.moerti.springprojects.RestAppSensor.repositories.SensorRepository;
import ru.moerti.springprojects.RestAppSensor.util.SensorNotFoundException;

import java.util.List;

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
            throw new IllegalArgumentException("Measure cannot be null");
        }
        // Безопасная проверка
        String sensorName = measure.getSensor() != null
                ? measure.getSensor().getName()
                : null;

        if (sensorName == null || !sensorRepository.existsByName(sensorName)) {
            throw new SensorNotFoundException(
                    "Sensor with name '" + sensorName + "' not found!"
            );
        }

        return measureRepository.save(measure);
    }

    public List<Measure> findAll() {

        return measureRepository.findAll();
    }

    public Long getRainyDaysCount() {
        return measureRepository.countByRainingTrue();
    }
}
