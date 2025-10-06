package com.agora.monitoring.sensor;

import com.agora.monitoring.model.TemperatureReading;

import java.util.List;

public interface SensorReader {
    List<TemperatureReading> getTemperatureReadings();
}
