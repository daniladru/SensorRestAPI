package com.danilyuk.SensorRestAPI.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class SensorDTO {
    @NotEmpty(message = "Sensor name must by fill")
    @Size(min = 3,max = 30,message = "Name size from 3 to 30")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SensorDTO(String name) {
        this.name = name;
    }
}
