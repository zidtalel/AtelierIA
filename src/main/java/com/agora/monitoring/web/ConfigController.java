package com.agora.monitoring.web;

import com.agora.monitoring.config.ConfigService;
import com.agora.monitoring.sensor.SensorReader;
import com.agora.monitoring.model.TemperatureReading;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

    private final ConfigService configService;
    private final SensorReader sensorReader;
    private final com.agora.monitoring.sensor.FanReader fanReader;

    public ConfigController(ConfigService configService, SensorReader sensorReader, com.agora.monitoring.sensor.FanReader fanReader) {
        this.configService = configService;
        this.sensorReader = sensorReader;
        this.fanReader = fanReader;
    }

    @PostMapping("/sensors/{id}")
    public ResponseEntity<?> updateThreshold(@PathVariable String id, @RequestBody Map<String, Object> body) {
        // Validate sensor existence before attempting to set threshold
        try {
            List<TemperatureReading> readings = sensorReader.getTemperatureReadings();
            boolean exists = readings.stream().anyMatch(r -> r.getId().equals(id));
            if (!exists) {
                return ResponseEntity.status(404).body("Sensor ID not found");
            }
        } catch (Exception e) {
            // If sensorReader fails, continue and let parsing/setting handle errors
        }

        if (body.containsKey("max")) {
            try {
                double max = Double.parseDouble(body.get("max").toString());
                configService.setMaxThreshold(id, max);
                configService.save();
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Invalid max value");
            }
        }
        return ResponseEntity.badRequest().body("Missing 'max' in body");
    }

    @PostMapping("/fans/{id}")
    public ResponseEntity<?> updateFanThreshold(@PathVariable String id, @RequestBody Map<String, Object> body) {
        if (body.containsKey("min")) {
            try {
                double min = Double.parseDouble(body.get("min").toString());
                // Validate fan existence
                try {
                    List<com.agora.monitoring.model.FanReading> fans = fanReader.getFanReadings();
                    boolean exists = fans.stream().anyMatch(f -> f.getId().equals(id));
                    if (!exists) {
                        return ResponseEntity.status(404).body("Fan ID not found");
                    }
                } catch (Exception e) {
                    // if fanReader fails, allow setting to proceed (best-effort)
                }

                configService.setMaxThreshold("fan:" + id + ":min", min);
                configService.save();
                return ResponseEntity.ok().build();
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Invalid min value");
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Failed to persist fan threshold");
            }
        }
        return ResponseEntity.badRequest().body("Missing 'min' in body");
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllThresholds() {
        return ResponseEntity.ok(configService.getAll());
    }
}
