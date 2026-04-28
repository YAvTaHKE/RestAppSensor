package ru.moerti.springprojects.RestAppSensor.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather/measurments")
public class MeasurmentsController {


    @PostMapping("/add")
    public String addMeasurments() {
        return null;
    }

    @GetMapping()
    public String getMeasurments() {
        return null;
    }

    @GetMapping("/rainyDaysCount")
    public String getRainyDaysCount(){
        return null;
    }
}
