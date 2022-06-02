package com.danilyuk.SensorRestAPI.util;

import com.danilyuk.SensorRestAPI.dto.MeasurementDTO;
import com.danilyuk.SensorRestAPI.models.Sensor;
import com.danilyuk.SensorRestAPI.services.MeasurementsService;
import com.danilyuk.SensorRestAPI.services.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MeasurementValidator implements Validator {
    private final SensorService sensorService;

    @Autowired
    public MeasurementValidator(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return MeasurementDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MeasurementDTO measurementDTO = (MeasurementDTO) target;
        Sensor foundSensor = sensorService.findByName(measurementDTO.getSensorDTO().getName());
        if (foundSensor == null) {
            errors.rejectValue("sensorDTO","","There is no sensor with this name");
        }
    }
}
