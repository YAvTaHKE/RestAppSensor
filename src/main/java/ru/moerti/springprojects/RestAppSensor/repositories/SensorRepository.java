package ru.moerti.springprojects.RestAppSensor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.moerti.springprojects.RestAppSensor.entity.Sensor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {

    boolean existsByName(String name);

    Optional<Sensor> findByName(String name);

    List<Sensor> findByNameIn(Collection<String> names);
}
