package com.agora.monitoring;

import com.agora.monitoring.alert.AlertService;
import com.agora.monitoring.config.ConfigService;
import com.agora.monitoring.monitor.FanMonitor;
import com.agora.monitoring.monitor.TemperatureMonitor;
import com.agora.monitoring.sensor.SimulatedFanReader;
import com.agora.monitoring.sensor.SimulatedSensorReader;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class MonitorsTest {

    @Test
    public void temperatureMonitorTriggersAlert() {
        SimulatedSensorReader reader = new SimulatedSensorReader();
        File f = new File("target/test-config-temp.json"); if (f.exists()) f.delete();
        ConfigService cfg = new ConfigService(f);
        // set low threshold to cause alerts
        cfg.setMaxThreshold("cpu0", 0.0);
        AlertService alert = new AlertService();
        TestAlertListener l = new TestAlertListener();
        alert.addListener(l);

        TemperatureMonitor tm = new TemperatureMonitor(reader, cfg, alert);
        var readings = tm.pollOnce();
        assertNotNull(readings);
        // since threshold is 0, at least one reading should trigger
        assertTrue(l.received);
    }

    @Test
    public void fanMonitorTriggersAlert() {
        SimulatedFanReader reader = new SimulatedFanReader();
        File f = new File("target/test-config-fan.json"); if (f.exists()) f.delete();
        ConfigService cfg = new ConfigService(f);
        // set high min threshold to force alert
        cfg.setMaxThreshold("fan:fan_chassis:min", 100000.0);
        AlertService alert = new AlertService();
        TestAlertListener l = new TestAlertListener();
        alert.addListener(l);

        FanMonitor fm = new FanMonitor(reader, cfg, alert);
        var readings = fm.pollOnce();
        assertNotNull(readings);
        assertTrue(l.received);
    }

    static class TestAlertListener implements AlertService.AlertListener {
        boolean received = false;

        @Override
        public void onAlert(String sensorId, double value, double threshold) {
            received = true;
        }
    }
}
