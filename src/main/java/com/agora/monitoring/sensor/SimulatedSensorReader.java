package com.agora.monitoring.sensor;

import com.agora.monitoring.model.TemperatureReading;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class SimulatedSensorReader implements SensorReader {
    @Override
    public List<TemperatureReading> getTemperatureReadings() {
        List<TemperatureReading> list = new ArrayList<>();
        Instant now = Instant.now();
    list.add(new TemperatureReading("cpu0", "CPU", 35.0, now, "SIMULATED"));
    list.add(new TemperatureReading("cpu0_core1", "CPU_CORE", 65.0, now, "SIMULATED"));
    list.add(new TemperatureReading("gpu0", "GPU", 55.0, now, "SIMULATED"));
    list.add(new TemperatureReading("hdd0", "HDD", 45.0, now, "SIMULATED"));
    list.add(new TemperatureReading("mb_temp", "MB", 40.0, now, "SIMULATED"));
        return list;
    }
}
