package com.danilyuk.SensorRestAPI.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

public class RainyDaysCountDTO {
    private Integer count;

    public RainyDaysCountDTO() {
    }

    public RainyDaysCountDTO(Integer count) {
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
