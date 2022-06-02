package com.danilyuk.SensorRestAPI.controllers;

import com.danilyuk.SensorRestAPI.dto.MeasurementDTO;
import com.danilyuk.SensorRestAPI.dto.RainyDaysCountDTO;
import com.danilyuk.SensorRestAPI.dto.SensorDTO;
import com.danilyuk.SensorRestAPI.models.Measurement;
import com.danilyuk.SensorRestAPI.services.MeasurementsService;
import com.danilyuk.SensorRestAPI.services.SensorService;
import com.danilyuk.SensorRestAPI.util.MeasurementValidator;
import com.danilyuk.SensorRestAPI.util.SensorErrorResponse;
import com.danilyuk.SensorRestAPI.util.SensorNotCreatedException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementsController {
    private final MeasurementsService measurementsService;
    private final SensorService sensorService;

    private final ModelMapper modelMapper;
    private final MeasurementValidator measurementValidator;

    @Autowired
    public MeasurementsController(MeasurementsService measurementsService, SensorService sensorService, ModelMapper modelMapper, MeasurementValidator measurementValidator) {
        this.measurementsService = measurementsService;
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
        this.measurementValidator = measurementValidator;
    }

    @GetMapping
    public List<MeasurementDTO> getALL(){
        return measurementsService.findAll().stream().map(this::convertToMeasurementDTO).collect(Collectors.toList());
    }

    @GetMapping("/rainyDaysCount")
    public RainyDaysCountDTO rainyDaysCount(){
        return new RainyDaysCountDTO(measurementsService.rainyDaysCount());
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> createSensor(@RequestBody @Valid MeasurementDTO measurementDTO, BindingResult bindingResult){
        measurementValidator.validate(measurementDTO,bindingResult);

        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError error :
                    bindingResult.getFieldErrors()) {
                errorMessage.append(error.getField())
                        .append(" : ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }
            throw new SensorNotCreatedException(errorMessage.toString());
        }
        measurementsService.saveMeasurement(convertToMeasurement(measurementDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handleException(SensorNotCreatedException e) {
        SensorErrorResponse response = new SensorErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO){
        Measurement measurement = modelMapper.map(measurementDTO, Measurement.class);
        measurement.setSensor(sensorService.findByName(measurementDTO.getSensorDTO().getName()));
        return measurement;
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement){
        MeasurementDTO measurementDTO = modelMapper.map(measurement, MeasurementDTO.class);
        SensorDTO sensorDTO = new SensorDTO(measurement.getSensor().getName());
        measurementDTO.setSensorDTO(sensorDTO);
        return measurementDTO;
    }

}
