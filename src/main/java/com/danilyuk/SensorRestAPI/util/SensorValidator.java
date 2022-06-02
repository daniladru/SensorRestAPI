package com.danilyuk.SensorRestAPI.util;

import com.danilyuk.SensorRestAPI.dto.SensorDTO;
import com.danilyuk.SensorRestAPI.models.Sensor;
import com.danilyuk.SensorRestAPI.services.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class SensorValidator implements Validator {
    private final SensorService sensorService;

    @Autowired
    public SensorValidator(SensorService sensorService) {
        this.sensorService = sensorService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return SensorDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SensorDTO sensorDTO = (SensorDTO) target;
        Sensor foundSensor = sensorService.findByName(sensorDTO.getName());
        if (foundSensor != null) {
            errors.rejectValue("name","","This sensor name is already taken");
        }
    }
}
