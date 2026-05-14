package ru.moerti.springprojects.RestAppSensor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.moerti.springprojects.RestAppSensor.entity.Measure;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MeasureRepository extends JpaRepository<Measure, Integer> {

    long countByRainingTrue();

    // метод для получения всех значений температуры
    @Query("SELECT m.value FROM Measure m WHERE m.sensor.name = :sensorName ORDER BY m.id ASC")
    List<BigDecimal> findAllValuesBySensorName(@Param("sensorName") String sensorName);


}
