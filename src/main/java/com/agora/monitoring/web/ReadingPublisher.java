package com.agora.monitoring.web;

import com.agora.monitoring.model.TemperatureReading;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;

public class ReadingPublisher {
    private static final Logger logger = LoggerFactory.getLogger(ReadingPublisher.class);

    private SimpMessagingTemplate template;
    private com.agora.monitoring.config.ConfigService configService;

    public ReadingPublisher() {
    }

    // Spring will inject messaging template if available
    public ReadingPublisher(SimpMessagingTemplate template) {
        this.template = template;
    }

    public ReadingPublisher(SimpMessagingTemplate template, com.agora.monitoring.config.ConfigService configService) {
        this.template = template;
        this.configService = configService;
    }

    public void setTemplate(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void publishTemperatureReadings(List<TemperatureReading> readings) {
        if (template == null) {
            // no websocket configured, just log
            logger.debug("Readings: {}", readings);
            return;
        }
        try {
            // convert to simple maps with alert/status using config thresholds
            java.util.List<java.util.Map<String, Object>> out = new java.util.ArrayList<>();
            for (TemperatureReading r : readings) {
                java.util.Map<String, Object> m = new java.util.HashMap<>();
                m.put("id", r.getId());
                m.put("type", r.getType());
                m.put("valueC", r.getValueC());
                m.put("source", r.getSource());
                m.put("timestamp", r.getTimestamp() == null ? null : r.getTimestamp().toString());
                Double thr = null;
                try {
                    if (configService != null) thr = configService.getMaxThreshold(r.getId());
                } catch (Exception ignored) {}
                boolean alert = (thr != null && r.getValueC() > thr);
                m.put("threshold", thr);
                m.put("alert", alert);
                m.put("status", alert ? "ALERT" : "OK");
                out.add(m);
            }
            template.convertAndSend("/topic/readings", out);
        } catch (Exception e) {
            logger.error("Failed to send readings via websocket", e);
        }
    }

    public void publishFanReadings(List<com.agora.monitoring.model.FanReading> fans) {
        if (template == null) {
            logger.debug("Fan readings: {}", fans);
            return;
        }
        try {
            java.util.List<java.util.Map<String, Object>> out = new java.util.ArrayList<>();
            for (com.agora.monitoring.model.FanReading f : fans) {
                java.util.Map<String, Object> m = new java.util.HashMap<>();
                m.put("id", f.getId());
                m.put("name", f.getName());
                m.put("rpm", f.getRpm());
                m.put("source", f.getSource());
                m.put("timestamp", f.getTimestamp() == null ? null : f.getTimestamp().toString());
                Double min = null;
                try {
                    if (configService != null) min = configService.getMaxThreshold("fan:" + f.getId() + ":min");
                } catch (Exception ignored) {}
                boolean alert = (min != null && f.getRpm() < min);
                m.put("threshold", min);
                m.put("alert", alert);
                m.put("status", alert ? "ALERT" : "OK");
                out.add(m);
            }
            template.convertAndSend("/topic/fans", out);
        } catch (Exception e) {
            logger.error("Failed to send fan readings via websocket", e);
        }
    }
}
