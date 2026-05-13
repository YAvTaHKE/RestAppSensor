package ru.moerti.springprojects.RestAppSensor.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "measure")
public class Measure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "value", nullable = false)
    @Min(value = -100, message = "Value should be greater or equal -100")
    @Max(value = 100, message = "Value should be less or equal 100")
    @NotNull
    private BigDecimal value;

    @Column(name = "raining", nullable = false)
    @NotNull
    private Boolean raining;

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id", referencedColumnName = "id")
    private Sensor sensor;

    public Measure() {
    }

    public Measure(BigDecimal value, Boolean raining) {
        this.value = value;
        this.raining = raining;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Boolean getRaining() {
        return raining;
    }

    public void setRaining(Boolean raining) {
        this.raining = raining;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Measure measure)) return false;

        // Если у обоих есть id - сравниваем по id
        if (id != null && measure.id != null) {
            return Objects.equals(id, measure.id);
        }

        // Иначе сравниваем по бизнес-полям
        return Objects.equals(value, measure.value) &&
                Objects.equals(raining, measure.raining) &&
                Objects.equals(createdAt, measure.createdAt) &&
                Objects.equals(sensor, measure.sensor);
    }

    @Override
    public int hashCode() {
        // Для сохраненных объектов
        if (id != null) {
            return Objects.hashCode(id);
        }

        // Для новых объектов - стабильная комбинация
        return Objects.hash(value, raining, createdAt, sensor);
    }

    @Override
    public String toString() {
        return "Measure{" +
                "id=" + id +
                ", value=" + value +
                ", raining=" + raining +
                ", createdAt=" + createdAt +
                ", sensorId=" + (sensor != null ? sensor.getId() : null) +
                '}';
    }
}