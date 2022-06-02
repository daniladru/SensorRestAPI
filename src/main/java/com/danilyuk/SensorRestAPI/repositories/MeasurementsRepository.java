package com.danilyuk.SensorRestAPI.repositories;

import com.danilyuk.SensorRestAPI.models.Measurement;
import com.danilyuk.SensorRestAPI.models.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeasurementsRepository extends JpaRepository<Measurement, Integer> {
    List<Measurement> findByRainingEquals(Boolean raining);
}
