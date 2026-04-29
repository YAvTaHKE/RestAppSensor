package ru.moerti.springprojects.RestAppSensor.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SensorDTO {

    @Column(name = "name", nullable = false, unique = true, length = 30)
    @NotBlank(message = "Name not should be empty")
    @Size(min = 3, max = 30, message = "Name should between 3 and 30 characters")
    private String name;

    public SensorDTO(){

    }

    public SensorDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SensorDTO{" +
                "name='" + name + '\'' +
                '}';
    }
}