package ru.moerti.springprojects.RestAppSensor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.moerti.springprojects.RestAppSensor.entity.Sensor;
import ru.moerti.springprojects.RestAppSensor.repositories.SensorRepository;
import ru.moerti.springprojects.RestAppSensor.util.SensorNotFoundException;
import ru.moerti.springprojects.RestAppSensor.util.SensorNotRegistrationException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SensorService {

    private final SensorRepository sensorRepository;

    @Autowired
    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public Sensor findById(int id) {
        return sensorRepository.findById(id)
                .orElseThrow(() -> new SensorNotFoundException("Sensor with id '" + id + "' not found"));
    }

    public Sensor findByName(String name) {
        return sensorRepository.findByName(name)
                .orElseThrow(() -> new SensorNotFoundException("Sensor with name '" + name + "' not found"));
    }

    public List<Sensor> findAll() {
        return sensorRepository.findAll();
    }

    @Transactional
    public Sensor save(Sensor sensor) {
        if (sensor == null) {
            throw new IllegalArgumentException("Sensor cannot be null");
        }

        if (sensorRepository.existsByName(sensor.getName())) {
            throw new SensorNotRegistrationException(
                    "Sensor with name '" + sensor.getName() + "' already exists!"
            );
        }

        return sensorRepository.save(sensor);
    }

    @Transactional
    public void deleteByName(String name) {
        Sensor sensor = findByName(name);  // Переиспользуем метод
        sensorRepository.delete(sensor);
    }

    @Transactional
    public void deleteById(int id) {
        Sensor sensor = findById(id);  // Переиспользуем метод
        sensorRepository.delete(sensor);
    }
}