package com.agora.monitoring.sensor;

import com.agora.monitoring.model.FanReading;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Sensors;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Real fan reader using OSHI. On platforms where fan sensors aren't exposed,
 * this will fall back to the simulated reader.
 */
public class RealFanReader implements FanReader {
    private final SimulatedFanReader fallback = new SimulatedFanReader();

    @Override
    public List<FanReading> getFanReadings() {
        String os = System.getProperty("os.name", "").toLowerCase();
        if (os.contains("win")) {
            try {
                WmiFanReader wmi = new WmiFanReader();
                List<FanReading> wmiVals = wmi.getFanReadings();
                if (wmiVals != null && !wmiVals.isEmpty()) return wmiVals;
            } catch (Throwable ignored) {}
        }

        try {
            SystemInfo si = new SystemInfo();
            HardwareAbstractionLayer hal = si.getHardware();
            Sensors sensors = hal.getSensors();
            // OSHI exposes fans as an array of integers
            int[] fans = sensors.getFanSpeeds();
            if (fans == null || fans.length == 0) {
                return fallback.getFanReadings();
            }
            // if all fan speeds are zero or negative, treat as not-available
            boolean anyPositive = Arrays.stream(fans).anyMatch(v -> v > 0);
            if (!anyPositive) {
                return fallback.getFanReadings();
            }
            List<FanReading> out = new ArrayList<>();
            Instant now = Instant.now();
            for (int i = 0; i < fans.length; i++) {
                int rpm = fans[i];
                // if this particular fan reading is not available, skip it
                if (rpm <= 0) continue;
                String id = "fan_" + i;
                String name = "Fan " + i;
                out.add(new FanReading(id, name, rpm, now, "REAL"));
            }
            return out;
        } catch (Throwable t) {
            // If OSHI isn't available or fails, fall back to the simulated
            return fallback.getFanReadings();
        }
    }
}
