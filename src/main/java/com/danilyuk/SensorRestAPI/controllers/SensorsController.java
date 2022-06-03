package com.danilyuk.SensorRestAPI.controllers;

import com.danilyuk.SensorRestAPI.dto.MeasurementDTO;
import com.danilyuk.SensorRestAPI.dto.SensorDTO;
import com.danilyuk.SensorRestAPI.models.Measurement;
import com.danilyuk.SensorRestAPI.models.Sensor;
import com.danilyuk.SensorRestAPI.services.MeasurementsService;
import com.danilyuk.SensorRestAPI.services.SensorService;
import com.danilyuk.SensorRestAPI.util.SensorErrorResponse;
import com.danilyuk.SensorRestAPI.util.SensorNotCreatedException;
import com.danilyuk.SensorRestAPI.util.SensorValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sensors")
public class SensorsController {
    private final SensorService sensorService;
    private final ModelMapper modelMapper;
    private final SensorValidator sensorValidator;
    private final MeasurementsService measurementsService;


    @Autowired
    public SensorsController(SensorService sensorService, ModelMapper modelMapper, SensorValidator sensorValidator, MeasurementsService measurementsService) {
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
        this.sensorValidator = sensorValidator;
        this.measurementsService = measurementsService;
    }

    @PostMapping("")
    public SensorDTO createSensor(@RequestBody @Valid SensorDTO sensorDTO, BindingResult bindingResult) {
        sensorValidator.validate(sensorDTO, bindingResult);

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
        Sensor sensor = sensorService.saveSensor(convertToSensor(sensorDTO));
        return convertToSensorDTO(sensor);
    }

    @PostMapping("/{id}/measurement")
    public ResponseEntity<HttpStatus> createMeasurement(@PathVariable("id") int sensorId, @RequestBody @Valid MeasurementDTO measurementDTO, BindingResult bindingResult) {
        Sensor foundSensor = sensorService.findById(sensorId);
        if (foundSensor == null) {
            bindingResult.reject("There is no sensor with this id");
        }
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();

            for (ObjectError error :
                    bindingResult.getAllErrors()) {
                errorMessage.append(error.getCode())
                        .append(";");
            }
            throw new SensorNotCreatedException(errorMessage.toString());
        }

        Measurement measurement = convertToMeasurement(measurementDTO);
        measurement.setSensor(foundSensor);
        measurementsService.saveMeasurement(measurement);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{id}/measurement")
    public List<MeasurementDTO> getALL(@PathVariable("id") int sensorId, @RequestParam(value = "raining", required = false) Boolean raining) {
        Sensor sensor = sensorService.findById(sensorId);
        if (raining == null) {
            return measurementsService.findBySensor(sensor).stream().map(this::convertToMeasurementDTO).collect(Collectors.toList());
        } else {
            return measurementsService.findBySensorAndRaining(sensor, raining).stream().map(this::convertToMeasurementDTO).collect(Collectors.toList());
        }
    }

    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handleException(SensorNotCreatedException e) {
        SensorErrorResponse response = new SensorErrorResponse(
                e.getMessage(),
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

    private Measurement convertToMeasurement(MeasurementDTO measurementDTO) {
        return modelMapper.map(measurementDTO, Measurement.class);
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }


}
