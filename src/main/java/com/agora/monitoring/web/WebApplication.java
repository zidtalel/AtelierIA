package com.agora.monitoring.web;

import com.agora.monitoring.alert.AlertService;
import com.agora.monitoring.config.ConfigService;
import com.agora.monitoring.monitor.TemperatureMonitor;
import com.agora.monitoring.sensor.SimulatedSensorReader;
import com.agora.monitoring.sensor.SensorReader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @Bean
    public ConfigService configService() {
        return new ConfigService(new File("config.json"));
    }

    @Bean
    public AlertService alertService() {
        return new AlertService();
    }

    @Bean
    public SensorReader sensorReader() {
        // Simulated by default for safe local testing; system has RealSensorReader if desired
        return new SimulatedSensorReader();
    }

    @Bean
    public TemperatureMonitor temperatureMonitor(SensorReader reader, ConfigService config, AlertService alert) {
        return new TemperatureMonitor(reader, config, alert);
    }

    @Bean
    public com.agora.monitoring.sensor.FanReader fanReader() {
        return new com.agora.monitoring.sensor.SimulatedFanReader();
    }

    @Bean
    public com.agora.monitoring.monitor.FanMonitor fanMonitor(com.agora.monitoring.sensor.FanReader reader,
                                                              ConfigService config, AlertService alert) {
        return new com.agora.monitoring.monitor.FanMonitor(reader, config, alert);
    }

    // polling handled by a dedicated Poller component (see Poller.java)
}
