package ru.moerti.springprojects.RestAppSensor.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.moerti.springprojects.RestAppSensor.dto.MeasureDTO;
import ru.moerti.springprojects.RestAppSensor.entity.Measure;
import ru.moerti.springprojects.RestAppSensor.services.MeasureService;
import ru.moerti.springprojects.RestAppSensor.util.MeasureErrorResponse;
import ru.moerti.springprojects.RestAppSensor.util.MeasureInvalidData;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/weather/measurements")
public class MeasurementController {

    private final MeasureService measureService;
    private final ModelMapper modelMapper;

    @Autowired
    public MeasurementController(MeasureService measureService, ModelMapper modelMapper) {
        this.measureService = measureService;
        this.modelMapper = modelMapper;
    }

    //Все измерения из БД
    @GetMapping()
    public ResponseEntity<List<MeasureDTO>> getMeasurements() {
        return new ResponseEntity<>(measureService.findAll()
                .stream().map(this::convertToMeasureDTO)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    //Добавляет измерения
    @PostMapping()
    public ResponseEntity<MeasureDTO> addMeasurements(@RequestBody @Valid MeasureDTO measureDTO,
                                                      BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + "-" + error.getDefaultMessage())
                    .collect(Collectors.joining(";"));
            throw new MeasureInvalidData(errorMsg);
        }

        Measure saveMeasure = measureService.save(convertToMeasure(measureDTO));

        return new ResponseEntity<>(convertToMeasureDTO(saveMeasure), HttpStatus.CREATED);
    }

    //Возвращает количество дождливых дней из БД
    @GetMapping("/rainyDaysCount")
    public ResponseEntity<Long> getRainyDaysCount() {
        return new ResponseEntity<>(measureService.getRainyDaysCount(), HttpStatus.OK);
    }

    private Measure convertToMeasure(MeasureDTO measureDTO) {
        return modelMapper.map(measureDTO, Measure.class);
    }

    private MeasureDTO convertToMeasureDTO(Measure measure) {
        return modelMapper.map(measure, MeasureDTO.class);
    }

    @ExceptionHandler(MeasureInvalidData.class)
    private ResponseEntity<MeasureErrorResponse> handleDataException(MeasureInvalidData exception) {
        MeasureErrorResponse response = new MeasureErrorResponse(
                exception.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<MeasureErrorResponse> handleIllegalArgumentException (IllegalArgumentException exception) {
        MeasureErrorResponse response = new MeasureErrorResponse(
                exception.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
