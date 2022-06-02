package com.danilyuk.SensorRestAPI.services;

import com.danilyuk.SensorRestAPI.models.Sensor;
import com.danilyuk.SensorRestAPI.repositories.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SensorService {
    private final SensorRepository sensorRepository;

    @Autowired
    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public Sensor findByName(String name){
        Optional<Sensor> sensor = sensorRepository.findByName(name);
        return sensor.orElse(null);
    }

    @Transactional
    public void saveSensor(Sensor sensor){
        enrichSensor(sensor);
        sensorRepository.save(sensor);
    }

    public void enrichSensor(Sensor sensor){
        sensor.setCreatedAt(LocalDateTime.now());
    }
}
