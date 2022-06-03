package com.danilyuk.SensorRestAPI.controllers;

import com.danilyuk.SensorRestAPI.dto.MeasurementDTO;
import com.danilyuk.SensorRestAPI.models.Measurement;
import com.danilyuk.SensorRestAPI.services.MeasurementsService;
import com.danilyuk.SensorRestAPI.services.SensorService;
import com.danilyuk.SensorRestAPI.util.SensorErrorResponse;
import com.danilyuk.SensorRestAPI.util.SensorNotCreatedException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/measurements")
public class MeasurementsController {
    private final MeasurementsService measurementsService;
    private final SensorService sensorService;

    private final ModelMapper modelMapper;

    @Autowired
    public MeasurementsController(MeasurementsService measurementsService, SensorService sensorService, ModelMapper modelMapper) {
        this.measurementsService = measurementsService;
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<MeasurementDTO> getALL(@RequestParam(value = "raining", required = false) Boolean raining) {
        if (raining == null) {
            return measurementsService.findAll().stream().map(this::convertToMeasurementDTO).collect(Collectors.toList());
        } else {
            return measurementsService.findByRaining(raining).stream().map(this::convertToMeasurementDTO).collect(Collectors.toList());
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

    private MeasurementDTO convertToMeasurementDTO(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDTO.class);
    }

}
