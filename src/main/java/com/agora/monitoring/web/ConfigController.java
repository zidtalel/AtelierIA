package com.agora.monitoring.web;

import com.agora.monitoring.config.ConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @PostMapping("/sensors/{id}")
    public ResponseEntity<?> updateThreshold(@PathVariable String id, @RequestBody Map<String, Object> body) {
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
                configService.setMaxThreshold("fan:" + id + ":min", min);
                configService.save();
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Invalid min value");
            }
        }
        return ResponseEntity.badRequest().body("Missing 'min' in body");
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllThresholds() {
        return ResponseEntity.ok(configService.getAll());
    }
}
