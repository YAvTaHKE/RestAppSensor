package ru.moerti.springprojects.RestAppSensor.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.moerti.springprojects.RestAppSensor.dto.SensorDTO;
import ru.moerti.springprojects.RestAppSensor.entity.Sensor;
import ru.moerti.springprojects.RestAppSensor.repositories.SensorRepository;
import ru.moerti.springprojects.RestAppSensor.util.SensorNotFoundException;
import ru.moerti.springprojects.RestAppSensor.util.SensorNotRegistrationException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SensorService {

    private SensorRepository sensorRepository;

    @Autowired
    public SensorService(SensorRepository sensorRepository) {
       this.sensorRepository = sensorRepository;
    }

    //Если такого сенсора нет в БД сохраняем новый
    @Transactional
    public void save(Sensor sensor) {
        if (sensorRepository.existsByName(sensor.getName())) {
            throw new SensorNotRegistrationException(
                    "Sensor with name '" + sensor.getName() + "' already exists!"
            );
        }
        sensorRepository.save(sensor);
    }

    public Sensor findById(int id) {
        return sensorRepository.findById(id).orElseThrow(SensorNotFoundException::new);
    }

    public List<Sensor> findAll() {
        return sensorRepository.findAll();
    }

    @Transactional
    public void delete(String name) {

        Optional<Sensor> optionalSensor = sensorRepository.findByName(name);

        optionalSensor.ifPresent(sensor -> sensorRepository.delete(sensor));
       sensorRepository.delete(optionalSensor.orElseThrow(SensorNotFoundException::new));
    }
}
