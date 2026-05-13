package ru.moerti.springprojects.RestAppSensor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.moerti.springprojects.RestAppSensor.entity.Measure;

@Repository
public interface MeasureRepository extends JpaRepository<Measure, Integer> {

    long countByRainingTrue();
}
