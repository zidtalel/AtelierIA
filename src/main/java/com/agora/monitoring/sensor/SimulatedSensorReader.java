package com.agora.monitoring.sensor;

import com.agora.monitoring.model.TemperatureReading;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

public class SimulatedSensorReader implements SensorReader {
    private final Map<String, Double> baseTemps = new HashMap<>();
    private final Random rnd = new Random();

    public SimulatedSensorReader() {
        // starting base temperatures (°C)
        baseTemps.put("cpu0", 35.0);
        baseTemps.put("cpu0_core1", 65.0);
        baseTemps.put("gpu0", 55.0);
        baseTemps.put("hdd0", 45.0);
        baseTemps.put("mb_temp", 40.0);
    }

    @Override
    public List<TemperatureReading> getTemperatureReadings() {
        List<TemperatureReading> list = new ArrayList<>();
        Instant now = Instant.now();

        // For each sensor, apply a small random jitter and slow drift to simulate
        // changing system temperatures similar to the fan simulation.
        for (Map.Entry<String, Double> e : baseTemps.entrySet()) {
            String id = e.getKey();
            double base = e.getValue();

            // jitter +/- up to 2.0 °C
            double jitter = (rnd.nextDouble() * 4.0) - 2.0;

            // occasional slow drift: +/- up to 0.2 °C
            double drift = (rnd.nextDouble() * 0.4) - 0.2;
            base += drift;
            // clamp reasonable temperature bounds
            if (base < -50.0) base = -50.0;
            if (base > 150.0) base = 150.0;
            e.setValue(base);

            double temp = Math.max(-50.0, base + jitter);
            // round to 2 decimal places
            double rounded = Math.round(temp * 100.0) / 100.0;

            String type;
            switch (id) {
                case "cpu0": type = "CPU"; break;
                case "cpu0_core1": type = "CPU_CORE"; break;
                case "gpu0": type = "GPU"; break;
                case "hdd0": type = "HDD"; break;
                case "mb_temp": type = "MB"; break;
                default: type = "TEMP"; break;
            }
            list.add(new TemperatureReading(id, type, rounded, now, "SIMULATED"));
        }
        return list;
    }
}
