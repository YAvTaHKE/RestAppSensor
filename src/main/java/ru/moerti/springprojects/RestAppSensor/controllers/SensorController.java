package ru.moerti.springprojects.RestAppSensor.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.moerti.springprojects.RestAppSensor.dto.SensorDTO;
import ru.moerti.springprojects.RestAppSensor.entity.Sensor;
import ru.moerti.springprojects.RestAppSensor.services.SensorService;
import ru.moerti.springprojects.RestAppSensor.util.MeasureErrorResponse;
import ru.moerti.springprojects.RestAppSensor.util.SensorErrorResponse;
import ru.moerti.springprojects.RestAppSensor.util.SensorNotFoundException;
import ru.moerti.springprojects.RestAppSensor.util.SensorNotRegistrationException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/weather/sensors")
public class SensorController {

    private final ModelMapper modelMapper;
    private final SensorService sensorService;

    @Autowired
    public SensorController(SensorService sensorService, ModelMapper modelMapper) {
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
    }

    //
    @GetMapping
    public List<SensorDTO> getSensors(){
        return sensorService.findAll()
                .stream()
                .map(this::convertToSensorDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public SensorDTO showSensorById(@PathVariable("id") int id) {
        return convertToSensorDTO(sensorService.findById(id));
    }

    @GetMapping("/name/{name}")
    public SensorDTO showSensorByName(@PathVariable("name") String name) {
        return convertToSensorDTO(sensorService.findByName(name));
    }

    @PostMapping("/registration")
    public ResponseEntity<SensorDTO> registerSensor(@RequestBody @Valid SensorDTO sensorDTO,
                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + "-" + error.getDefaultMessage())
                    .collect(Collectors.joining(";"));
            throw new SensorNotRegistrationException(errorMsg);
        }

        Sensor savedSensor = sensorService.save(convertToSensor(sensorDTO));

        return new ResponseEntity<>(convertToSensorDTO(savedSensor), HttpStatus.CREATED);
    }

    @DeleteMapping("/name/{name}")
    public ResponseEntity<HttpStatus> deleteByName(@PathVariable("name") String name) {
        sensorService.deleteByName(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable("id") int id) {
        sensorService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(SensorNotRegistrationException.class)
    private ResponseEntity<SensorErrorResponse> handleRegistrationException(SensorNotRegistrationException exception) {
        SensorErrorResponse response = new SensorErrorResponse(
                exception.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SensorNotFoundException.class)
    private ResponseEntity<SensorErrorResponse> handleNotFoundException (SensorNotFoundException exception) {
        SensorErrorResponse response = new SensorErrorResponse(
                exception.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<SensorErrorResponse> handleValidationExceptions(MethodArgumentNotValidException exception) {
        String errorMsg = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        SensorErrorResponse response = new SensorErrorResponse(errorMsg, System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    private ResponseEntity<SensorErrorResponse> handleIllegalStateException (IllegalStateException exception) {
        SensorErrorResponse response = new SensorErrorResponse(
                exception.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Sensor convertToSensor(SensorDTO sensorDTO) {
        return modelMapper.map(sensorDTO, Sensor.class);
    }

    private SensorDTO convertToSensorDTO(Sensor sensor) {
        return modelMapper.map(sensor, SensorDTO.class);
    }
}
