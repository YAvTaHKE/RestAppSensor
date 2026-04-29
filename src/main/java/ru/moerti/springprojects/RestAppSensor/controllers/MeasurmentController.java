package ru.moerti.springprojects.RestAppSensor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.moerti.springprojects.RestAppSensor.entity.Measure;
import ru.moerti.springprojects.RestAppSensor.services.MeasureService;

import java.util.List;

@RestController
@RequestMapping("/weather/measurments")
public class MeasurmentController {

    private MeasureService measureService;

    @Autowired
    public MeasurmentController(MeasureService measureService) {
        this.measureService = measureService;
    }

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
