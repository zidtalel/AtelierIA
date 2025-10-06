package com.agora.monitoring.monitor;

import com.agora.monitoring.alert.AlertService;
import com.agora.monitoring.config.ConfigService;
import com.agora.monitoring.model.FanReading;
import com.agora.monitoring.sensor.FanReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FanMonitor {
    private static final Logger logger = LoggerFactory.getLogger(FanMonitor.class);

    private final FanReader reader;
    private final ConfigService config;
    private final AlertService alertService;

    public FanMonitor(FanReader reader, ConfigService config, AlertService alertService) {
        this.reader = reader;
        this.config = config;
        this.alertService = alertService;
    }

    public List<FanReading> pollOnce() {
        List<FanReading> readings = reader.getFanReadings();
        for (FanReading r : readings) {
            Double min = config.getMaxThreshold("fan:" + r.getId() + ":min");
            int rpm = r.getRpm();
            boolean alert = false;
            if (min != null && rpm < min) alert = true;
            if (alert) {
                logger.warn("Fan {} ALERTE rpm={} (min={})", r.getId(), rpm, min);
                alertService.alert(r.getId(), rpm, min == null ? Double.NaN : min);
            } else {
                logger.info("Fan {} OK rpm={}", r.getId(), rpm);
            }
        }
        return readings;
    }
}
