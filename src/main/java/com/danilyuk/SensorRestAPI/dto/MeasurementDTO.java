package com.danilyuk.SensorRestAPI.dto;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

public class MeasurementDTO {
    @NotNull
    @Range(min = -100,max = 100,message = "from -100 to 100")
    private Float value;

    @NotNull
    private Boolean raining;

    public MeasurementDTO() {
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Boolean getRaining() {
        return raining;
    }

    public void setRaining(Boolean raining) {
        this.raining = raining;
    }
}
