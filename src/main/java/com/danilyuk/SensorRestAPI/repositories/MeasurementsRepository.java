package com.danilyuk.SensorRestAPI.repositories;

import com.danilyuk.SensorRestAPI.models.Measurement;
import com.danilyuk.SensorRestAPI.models.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.expression.spel.ast.BooleanLiteral;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeasurementsRepository extends JpaRepository<Measurement, Integer> {
    List<Measurement> findByRainingEquals(Boolean raining);
    List<Measurement> findBySensorEquals(Sensor sensor);
    List<Measurement> findBySensorEqualsAndRainingEquals(Sensor sensor, Boolean raining);
}
