package com.danilyuk.SensorRestAPI.services;

import com.danilyuk.SensorRestAPI.models.Measurement;
import com.danilyuk.SensorRestAPI.models.Sensor;
import com.danilyuk.SensorRestAPI.repositories.MeasurementsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class MeasurementsService {
    private final MeasurementsRepository measurementsRepository;

    @Autowired
    public MeasurementsService(MeasurementsRepository measurementsRepository) {
        this.measurementsRepository = measurementsRepository;
    }

    public List<Measurement> findAll() {
        return measurementsRepository.findAll();
    }

    public Integer rainyDaysCount() {
        List<Measurement> measurementList = measurementsRepository.findByRainingEquals(true);
        return measurementList.size();
    }

    @Transactional
    public void saveMeasurement(Measurement measurement) {
        enrichMeasurement(measurement);
        measurementsRepository.save(measurement);
    }

    public void enrichMeasurement(Measurement measurement) {
        measurement.setAddedAt(LocalDateTime.now());
    }

    public Map<String, Float> dataForChart() {
        Map<String, Float> graphData = new TreeMap<>();
        for (Measurement measurement :
                measurementsRepository.findAll()) {
            graphData.put(measurement.getAddedAt().format(DateTimeFormatter.ISO_DATE_TIME), measurement.getValue());
        }
        return graphData;
    }
}
