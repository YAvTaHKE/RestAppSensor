package ru.moerti.springprojects.RestAppSensor.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather/sensors")
public class SensorsController {

    @PostMapping("/registration")
    public String registerSersor() {

        return null;
    }
}
