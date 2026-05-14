package ru.moerti.springprojects.RestAppSensor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.moerti.springprojects.RestAppSensor.entity.Measure;
import ru.moerti.springprojects.RestAppSensor.entity.Sensor;
import ru.moerti.springprojects.RestAppSensor.repositories.MeasureRepository;
import ru.moerti.springprojects.RestAppSensor.repositories.SensorRepository;
import ru.moerti.springprojects.RestAppSensor.util.SensorNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    @Transactional
    public List<Measure> saveAll(List<Measure> measures) {
        if (measures == null || measures.isEmpty()) {
            return new ArrayList<>();
        }

        // Проверяем все сенсоры один раз
        Set<String> sensorNames = measures.stream()
                .map(m -> m.getSensor().getName())
                .collect(Collectors.toSet());

        Map<String, Sensor> existingSensors = sensorRepository.findByNameIn(sensorNames)
                .stream()
                .collect(Collectors.toMap(Sensor::getName, Function.identity()));

        for (Measure measure : measures) {
            String sensorName = measure.getSensor().getName();
            Sensor sensor = existingSensors.get(sensorName);
            if (sensor == null) {
                throw new SensorNotFoundException("Sensor not found: " + sensorName);
            }
            measure.setSensor(sensor);
        }

        return measureRepository.saveAll(measures); // Один запрос к БД!
    }

    public List<Measure> findAll() {

        return measureRepository.findAll();
    }

    public Long getRainyDaysCount() {
        return measureRepository.countByRainingTrue();
    }
}
