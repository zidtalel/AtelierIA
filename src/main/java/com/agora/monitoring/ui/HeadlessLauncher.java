package com.agora.monitoring.ui;

import com.agora.monitoring.alert.AlertService;
import com.agora.monitoring.config.ConfigService;
import com.agora.monitoring.monitor.FanMonitor;
import com.agora.monitoring.monitor.TemperatureMonitor;
import com.agora.monitoring.sensor.SimulatedFanReader;
import com.agora.monitoring.sensor.SimulatedSensorReader;

import java.io.File;

public class HeadlessLauncher {
    public static void main(String[] args) {
        ConfigService config = new ConfigService(new File("config.json"));
        AlertService alert = new AlertService();
        // prefer real readers in headless mode too
        TemperatureMonitor tmon = new TemperatureMonitor(new com.agora.monitoring.sensor.RealSensorReader(), config, alert);
    // use RealFanReader which will fall back to simulated values if OSHI isn't available
    com.agora.monitoring.sensor.FanReader fanReader = new com.agora.monitoring.sensor.RealFanReader();
    FanMonitor fmon = new FanMonitor(fanReader, config, alert);

        System.out.println("Polling temperature sensors...");
        tmon.pollOnce();
        System.out.println("Polling fan sensors...");
        fmon.pollOnce();
        System.out.println("Done.");
    }
}
