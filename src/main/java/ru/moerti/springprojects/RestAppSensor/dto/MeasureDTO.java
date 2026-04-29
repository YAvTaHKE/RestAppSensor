package ru.moerti.springprojects.RestAppSensor.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
public class MeasureDTO {

    @NotNull(message = "Value cannot be null")
    @Min(value = -100, message = "Value should be greater than or equal -100")
    @Max(value = 100, message = "Value should be less than or equal 100")
    private BigDecimal value;

    @NotNull(message = "Raining cannot be null")
    private Boolean raining;

    @Column(name = "name", nullable = false, unique = true, length = 30)
    @NotBlank(message = "Name not should be empty")
    @Size(min = 3, max = 30, message = "Name should between 3 and 30 characters")
    private String  sensorName;

    public MeasureDTO() {
    }

    public MeasureDTO(BigDecimal value, Boolean raining, String sensorName) {
        this.value = value;
        this.raining = raining;
        this.sensorName = sensorName;
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

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    @Override
    public String toString() {
        return "MeasureDTO{" +
                "value=" + value +
                ", raining=" + raining +
                ", sensorId=" + sensorName +
                '}';
    }
}
