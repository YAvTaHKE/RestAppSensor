package ru.moerti.springprojects.RestAppSensor.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.moerti.springprojects.RestAppSensor.dto.SensorDTO;
import ru.moerti.springprojects.RestAppSensor.entity.Sensor;
import ru.moerti.springprojects.RestAppSensor.services.SensorService;
import ru.moerti.springprojects.RestAppSensor.util.SensorErrorResponce;

import java.util.List;

@RestController
@RequestMapping("/weather/sensors")
public class SensorController {

    private final ModelMapper modelMapper;
    private SensorService sensorService;

    @Autowired
    public SensorController(SensorService sensorService, ModelMapper modelMapper) {
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registerSensor(@RequestBody @Valid SensorDTO sensorDTO,
                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg.append(error.getField())
                        .append("-")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw new SensorNotRegistrationExceprion(errorMsg.toString());
        }

        SensorService.save(convertToSensor(SensorDTO));

        return ResponseEntity.ok(HttpsStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<SensorErrorResponce> handleException(SensorNotFoundExceprion exceprion) {
        SensorErrorResponce responce = new SensorErrorResponce(
                "Sensor with this id wasn't found!",
                System.currentTimeMillis()
        );
        //В HTTP ответе тело ответа (responce) и статус в заголовке
        return new ResponseEntity<>(responce, HttpStatus.BAD_REQUEST);
    }

    private Sensor convertToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }

    private SensorDTO convertToSensorDTO(Sensor sensor) {
        return modelMapper.map(sensor, SensorDTO.class);
    }
}
