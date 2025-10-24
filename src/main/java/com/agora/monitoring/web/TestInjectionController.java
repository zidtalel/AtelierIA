package com.agora.monitoring.web;

import com.agora.monitoring.sensor.SimulatedSensorReader;
import com.agora.monitoring.sensor.SimulatedFanReader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestInjectionController {

    private final SimulatedSensorReader sensorReader;
    private final SimulatedFanReader fanReader;

    public TestInjectionController(SimulatedSensorReader sensorReader, SimulatedFanReader fanReader) {
        this.sensorReader = sensorReader;
        this.fanReader = fanReader;
    }

    @PostMapping("/sensors/{id}")
    public ResponseEntity<?> setSensorBase(@PathVariable String id, @RequestBody Map<String, Object> body) {
        if (!body.containsKey("value")) return ResponseEntity.badRequest().body("Missing 'value'");
        try {
            double v = Double.parseDouble(body.get("value").toString());
            sensorReader.setBaseTemp(id, v);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid value");
        }
    }

    @PostMapping("/fans/{id}")
    public ResponseEntity<?> setFanBase(@PathVariable String id, @RequestBody Map<String, Object> body) {
        if (!body.containsKey("rpm")) return ResponseEntity.badRequest().body("Missing 'rpm'");
        try {
            int rpm = Integer.parseInt(body.get("rpm").toString());
            fanReader.setBaseRpm(id, rpm);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid rpm");
        }
    }
}
