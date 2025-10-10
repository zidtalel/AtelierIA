package com.agora.monitoring.web;

import com.agora.monitoring.model.TemperatureReading;
import com.agora.monitoring.monitor.TemperatureMonitor;
import com.agora.monitoring.sensor.SensorReader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sensors")
public class SensorController {

    private final SensorReader reader;
    private final TemperatureMonitor monitor;

    public SensorController(SensorReader reader, TemperatureMonitor monitor) {
        this.reader = reader;
        this.monitor = monitor;
    }

    @GetMapping
    public List<TemperatureReading> list() {
        // return latest readings
        return reader.getTemperatureReadings();
    }

    @GetMapping("/{id}")
    public List<TemperatureReading> history(@PathVariable String id) {
        // for simplicity return current readings filtered by id
        return reader.getTemperatureReadings().stream().filter(r -> r.getId().equals(id)).toList();
    }
}
