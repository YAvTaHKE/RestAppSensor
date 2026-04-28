package ru.moerti.springprojects.RestAppSensor.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "sensor")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 30)
    @NotBlank(message = "Name not should be empty")
    @Size(min = 3, max = 30, message = "Name should between 3 and 30 characters")
    private String name;

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createdAt;


    public Sensor(){

    }

    public Sensor(String name) {
        this.name = name;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    @Override
    public String toString() {
        return String.format("Sensor{id=%d, name='%s', createdAt=%s}",
                id, name, createdAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sensor sensor)) return false;
        return Objects.equals(id, sensor.id) &&
                Objects.equals(name, sensor.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
