package com.danilyuk.SensorRestAPI.dto;

import com.danilyuk.SensorRestAPI.models.Sensor;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

public class MeasurementDTO {
    @NotNull
    @Range(min = -100,max = 100,message = "from -100 to 100")
    private Float value;

    @NotNull
    private Boolean raining;

    @JsonProperty("sensor")
    private SensorDTO sensorDTO;

    public MeasurementDTO() {
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public boolean isRaining() {
        return raining;
    }

    public void setRaining(boolean raining) {
        this.raining = raining;
    }

    public SensorDTO getSensorDTO() {
        return sensorDTO;
    }

    public void setSensorDTO(SensorDTO sensorDTO) {
        this.sensorDTO = sensorDTO;
    }
}
