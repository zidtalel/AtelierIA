package com.agora.monitoring.sensor;

import com.agora.monitoring.model.FanReading;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A small simulated fan reader that returns dynamic RPM values.
 * Previously this class returned fixed constants (1200/2000/800) which made
 * the UI show unchanging numbers. We now keep a small internal state per fan
 * and apply jitter + slow drift so the GUI and monitors can react to changing values.
 */
public class SimulatedFanReader implements FanReader {
    private final Map<String, Integer> baseRpms = new HashMap<>();
    private final Random rnd = new Random();

    public SimulatedFanReader() {
        // starting base RPMs (same as previous static values)
        baseRpms.put("fan_cpu", 1200);
        baseRpms.put("fan_gpu", 2000);
        baseRpms.put("fan_chassis", 800);
    }

    public List<FanReading> getFanReadings() {
        List<FanReading> out = new ArrayList<>();
        Instant now = Instant.now();

        // For each fan, apply a small random jitter and slow drift to simulate
        // changing system behavior.
        for (Map.Entry<String, Integer> e : baseRpms.entrySet()) {
            String id = e.getKey();
            int base = e.getValue();

            // jitter +/- 150 RPM
            int jitter = rnd.nextInt(301) - 150;

            // occasional slow drift: +/- 0..10 RPM
            int drift = rnd.nextInt(11) - 5;
            base += drift;
            if (base < 0) base = 0;
            e.setValue(base);

            int rpm = Math.max(0, base + jitter);
            String name;
            if ("fan_cpu".equals(id)) {
                name = "CPU Fan";
            } else if ("fan_gpu".equals(id)) {
                name = "GPU Fan";
            } else if ("fan_chassis".equals(id)) {
                name = "Chassis Fan";
            } else {
                name = "Fan";
            }
            out.add(new FanReading(id, name, rpm, now, "SIMULATED"));
        }
        return out;
    }
}
