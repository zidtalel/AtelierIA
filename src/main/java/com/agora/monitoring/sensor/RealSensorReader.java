package com.agora.monitoring.sensor;

import com.agora.monitoring.model.TemperatureReading;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Sensors;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Real sensor reader using OSHI for temperatures. If OSHI cannot provide sensor data,
 * this reader will return an empty list when no real readings are available.
 */
public class RealSensorReader implements SensorReader {
    private final SimulatedSensorReader fallback = new SimulatedSensorReader();
    private final boolean allowFallback;

    public RealSensorReader() {
        this(true);
    }

    public RealSensorReader(boolean allowFallback) {
        this.allowFallback = allowFallback;
    }

    @Override
    public List<TemperatureReading> getTemperatureReadings() {
        try {
            SystemInfo si = new SystemInfo();
            HardwareAbstractionLayer hal = si.getHardware();
            Sensors sensors = hal.getSensors();

            double cpuTemp = sensors.getCpuTemperature();
            // if cpuTemp is NaN or non-positive it's probably not available on this platform
            if (Double.isNaN(cpuTemp) || cpuTemp <= 0.0) {
                if (allowFallback) {
                    return fallback.getTemperatureReadings();
                } else {
                    // return a REAL reading with NaN value so the UI shows REAL but no number
                    List<TemperatureReading> out = new ArrayList<>();
                    Instant now = Instant.now();
                    out.add(new TemperatureReading("cpu0", "CPU", Double.NaN, now, "REAL"));
                    return out;
                }
            }
            // Map CPU temperature and keep fallback values for the rest.
            List<TemperatureReading> out = new ArrayList<>();
            Instant now = Instant.now();
            out.add(new TemperatureReading("cpu0", "CPU", cpuTemp, now, "REAL"));
            List<TemperatureReading> fallbackList = fallback.getTemperatureReadings();
            for (TemperatureReading tr : fallbackList) {
                if (!"cpu0".equals(tr.getId())) out.add(tr);
            }
            return out;
        } catch (Throwable t) {
            return fallback.getTemperatureReadings();
        }
    }
}
