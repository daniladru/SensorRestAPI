package com.danilyuk.SensorRestAPI.services;

import com.danilyuk.SensorRestAPI.models.Measurement;
import com.danilyuk.SensorRestAPI.models.Sensor;
import com.danilyuk.SensorRestAPI.repositories.MeasurementsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class MeasurementsService {
    private final MeasurementsRepository measurementsRepository;
    private final SensorService sensorService;

    @Autowired
    public MeasurementsService(MeasurementsRepository measurementsRepository, SensorService sensorService) {
        this.measurementsRepository = measurementsRepository;
        this.sensorService = sensorService;
    }

    public List<Measurement> findAll() {
        return measurementsRepository.findAll();
    }

    public List<Measurement> findBySensor(Sensor sensor) {
        return measurementsRepository.findBySensorEquals(sensor);
    }

    public List<Measurement> findBySensorAndRaining(Sensor sensor, Boolean raining) {
        return measurementsRepository.findBySensorEqualsAndRainingEquals(sensor, raining);
    }

    public List<Measurement> findByRaining(Boolean raining) {
        return measurementsRepository.findByRainingEquals(raining);
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

    public Map<String, Float> dataForChart(int sensorId, Boolean raining) {
        Sensor sensor = sensorService.findById(sensorId);
        List<Measurement> measurementList;
        if (raining == null) {
            measurementList = measurementsRepository.findBySensorEquals(sensor);
        } else {
            measurementList = measurementsRepository.findBySensorEqualsAndRainingEquals(sensor, raining);
        }

        Map<String, Float> graphData = new TreeMap<>();
        for (Measurement measurement :
                measurementList) {
            graphData.put(measurement.getAddedAt().format(DateTimeFormatter.ISO_DATE_TIME), measurement.getValue());
        }
        return graphData;
    }
}
