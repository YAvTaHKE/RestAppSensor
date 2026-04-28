package ru.moerti.springprojects.RestAppSensor.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.moerti.springprojects.RestAppSensor.entity.Measure;

import java.util.List;

@RestController
@RequestMapping("/weather/measurments")
public class MeasurmentController {


    @PostMapping("/add")
    public String addMeasurments() {
        return null;
    }

    @GetMapping()
    public List<Measure> getMeasurments() {
        return null;
    }

    @GetMapping("/rainyDaysCount")
    public String getRainyDaysCount(){
        return null;
    }
}
