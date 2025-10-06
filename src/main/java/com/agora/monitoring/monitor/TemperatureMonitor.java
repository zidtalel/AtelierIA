package com.agora.monitoring.monitor;

import com.agora.monitoring.alert.AlertService;
import com.agora.monitoring.config.ConfigService;
import com.agora.monitoring.model.TemperatureReading;
import com.agora.monitoring.sensor.SensorReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TemperatureMonitor {
    private static final Logger logger = LoggerFactory.getLogger(TemperatureMonitor.class);

    private final SensorReader reader;
    private final ConfigService config;
    private final AlertService alertService;

    public TemperatureMonitor(SensorReader reader, ConfigService config, AlertService alertService) {
        this.reader = reader;
        this.config = config;
        this.alertService = alertService;
    }

    public List<TemperatureReading> pollOnce() {
        List<TemperatureReading> readings = reader.getTemperatureReadings();
        for (TemperatureReading r : readings) {
            Double max = config.getMaxThreshold(r.getId());
            if (max != null && r.getValueC() > max) {
                logger.warn("Sensor {} over threshold: {} > {}", r.getId(), r.getValueC(), max);
                alertService.alert(r.getId(), r.getValueC(), max);
            } else {
                logger.info("Sensor {} OK: {}C", r.getId(), r.getValueC());
            }
        }
        return readings;
    }
}
