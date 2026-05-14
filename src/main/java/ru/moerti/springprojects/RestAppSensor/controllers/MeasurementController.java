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
import ru.moerti.springprojects.RestAppSensor.entity.Sensor;
import ru.moerti.springprojects.RestAppSensor.services.MeasureService;
import ru.moerti.springprojects.RestAppSensor.services.SensorService;
import ru.moerti.springprojects.RestAppSensor.util.MeasureErrorResponse;
import ru.moerti.springprojects.RestAppSensor.util.MeasureInvalidData;
import ru.moerti.springprojects.RestAppSensor.util.SensorErrorResponse;
import ru.moerti.springprojects.RestAppSensor.util.SensorNotFoundException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/weather/measurements")
public class MeasurementController {

    private final MeasureService measureService;
    private final ModelMapper modelMapper;
    private final SensorService sensorService;

    @Autowired
    public MeasurementController(MeasureService measureService, ModelMapper modelMapper, SensorService sensorService) {
        this.measureService = measureService;
        this.modelMapper = modelMapper;
        this.sensorService = sensorService;
    }

    //Все измерения из БД
    @GetMapping()
    public ResponseEntity<List<MeasureDTO>> getMeasurements() {
        return new ResponseEntity<>(measureService.findAll()
                .stream().map(this::convertToMeasureDTO)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    //Добавляет измерение
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

    //Добавляет измерения пакетом
    @PostMapping("/batch")
    public ResponseEntity<List<MeasureDTO>> addBatchMeasurements(@RequestBody List<MeasureDTO> measureDTOs) {

        // 1. Проверка на пустой список
        if (measureDTOs == null || measureDTOs.isEmpty()) {
            throw new MeasureInvalidData("Batch cannot be empty");
        }

        // 2. Валидация каждого элемента
        List<String> validationErrors = new ArrayList<>();
        for (int i = 0; i < measureDTOs.size(); i++) {
            MeasureDTO dto = measureDTOs.get(i);

            if (dto.getValue() == null ||
                    dto.getValue().compareTo(new BigDecimal("-100")) < 0 ||
                    dto.getValue().compareTo(new BigDecimal("100")) > 0) {
                validationErrors.add(String.format("[%d]: value must be between -100 and 100 (got %s)",
                        i, dto.getValue()));
            }

            if (dto.getSensorName() == null || dto.getSensorName().trim().isEmpty()) {
                validationErrors.add(String.format("[%d]: sensorName is required", i));
            }

            if (dto.getRaining() == null) {
                validationErrors.add(String.format("[%d]: raining is required", i));
            }
        }

        if (!validationErrors.isEmpty()) {
            throw new MeasureInvalidData(String.join("; ", validationErrors));
        }

        // 3. Сохранение
        List<Measure> measures = measureDTOs.stream()
                .map(this::convertToMeasure)
                .collect(Collectors.toList());

        List<Measure> savedMeasures = measureService.saveAll(measures);

        // 4. Возврат результата
        List<MeasureDTO> response = savedMeasures.stream()
                .map(this::convertToMeasureDTO)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //Возвращает количество дождливых дней из БД
    @GetMapping("/rainyDaysCount")
    public ResponseEntity<Long> getRainyDaysCount() {
        return new ResponseEntity<>(measureService.getRainyDaysCount(), HttpStatus.OK);
    }

    private Measure convertToMeasure(MeasureDTO measureDTO) {
        Measure measure =  modelMapper.map(measureDTO, Measure.class);

        // Находим сенсор по имени из DTO
        if (measureDTO.getSensorName() != null && !measureDTO.getSensorName().isEmpty()) {
            Sensor sensor = sensorService.findByName(measureDTO.getSensorName());
            measure.setSensor(sensor);
        }
        return measure;
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

    @ExceptionHandler(SensorNotFoundException.class)
    private ResponseEntity<SensorErrorResponse> handleNotFoundException (SensorNotFoundException exception) {
        SensorErrorResponse response = new SensorErrorResponse(
                exception.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
