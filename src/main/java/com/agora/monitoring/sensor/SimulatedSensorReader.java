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
        // Add numeric sensor IDs used by tests 100-199 range
        baseTemps.put("101", 36.0);
        baseTemps.put("102", 37.0);
        baseTemps.put("103", 38.0);
    }

    /**
     * Test helper: set the base temperature for a given sensor id.
     * This allows integration tests to inject deterministic readings.
     */
    public void setBaseTemp(String id, double temp) {
        synchronized (baseTemps) {
            baseTemps.put(id, temp);
        }
    }

    /**
     * Test helper: get current base temperature map copy.
     */
    public java.util.Map<String, Double> getBaseTemps() {
        synchronized (baseTemps) {
            return new java.util.HashMap<>(baseTemps);
        }
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
