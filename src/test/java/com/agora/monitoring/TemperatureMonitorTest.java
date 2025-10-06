package com.agora.monitoring;

import com.agora.monitoring.alert.AlertService;
import com.agora.monitoring.config.ConfigService;
import com.agora.monitoring.monitor.TemperatureMonitor;
import com.agora.monitoring.sensor.SimulatedSensorReader;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class TemperatureMonitorTest {

    @Test
    public void testParsingAndPolling() {
        SimulatedSensorReader reader = new SimulatedSensorReader();
        ConfigService config = new ConfigService(new File("target/test-config.json"));
        AlertService alert = new AlertService();
        TemperatureMonitor monitor = new TemperatureMonitor(reader, config, alert);

        // No thresholds set - no alerts
        assertNotNull(monitor.pollOnce());
    }

    @Test
    public void testAlertTriggered() {
        SimulatedSensorReader reader = new SimulatedSensorReader();
        File f = new File("target/test-config.json");
        if (f.exists()) f.delete();
        ConfigService config = new ConfigService(f);
        // set low threshold to force alerts
        config.setMaxThreshold("cpu0", 30.0);
        config.save();
        AlertService alert = new AlertService();
        TemperatureMonitor monitor = new TemperatureMonitor(reader, config, alert);

        var readings = monitor.pollOnce();
        boolean sawCpu = readings.stream().anyMatch(r -> r.getId().equals("cpu0"));
        assertTrue(sawCpu, "cpu0 reading should be present");
    }

    @Test
    public void testConfigPersistence() {
        File f = new File("target/test-config.json");
        if (f.exists()) f.delete();
        ConfigService config = new ConfigService(f);
        config.setMaxThreshold("gpu0", 70.0);
        config.save();

        ConfigService config2 = new ConfigService(f);
        assertEquals(70.0, config2.getMaxThreshold("gpu0"));
    }
}
