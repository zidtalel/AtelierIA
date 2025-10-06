package com.agora.monitoring;

import com.agora.monitoring.alert.AlertService;
import com.agora.monitoring.config.ConfigService;
import com.agora.monitoring.monitor.FanMonitor;
import com.agora.monitoring.sensor.SimulatedFanReader;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class FanMonitorTest {

    @Test
    public void testFanReadings() {
    com.agora.monitoring.sensor.SimulatedFanReader reader = new com.agora.monitoring.sensor.SimulatedFanReader();
        ConfigService config = new ConfigService(new File("target/test-fan-config.json"));
        AlertService alert = new AlertService();
        FanMonitor monitor = new FanMonitor(reader, config, alert);
        assertNotNull(monitor.pollOnce());
    }

    @Test
    public void testFanAlertLow() {
    com.agora.monitoring.sensor.SimulatedFanReader reader = new com.agora.monitoring.sensor.SimulatedFanReader();
        File f = new File("target/test-fan-config.json");
        if (f.exists()) f.delete();
        ConfigService config = new ConfigService(f);
        // set a high min to cause alert for chassis
        config.setMaxThreshold("fan:fan_chassis:min", 1000.0);
        config.save();
        AlertService alert = new AlertService();
        FanMonitor monitor = new FanMonitor(reader, config, alert);
        var readings = monitor.pollOnce();
        assertTrue(readings.stream().anyMatch(r -> r.getId().equals("fan_chassis")));
    }
}
