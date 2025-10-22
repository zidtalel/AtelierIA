package com.agora.monitoring.web;

import com.agora.monitoring.model.FanReading;
import com.agora.monitoring.sensor.FanReader;
import com.agora.monitoring.monitor.FanMonitor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fans")
public class FanController {

    private final FanReader reader;
    private final FanMonitor monitor;

    public FanController(FanReader reader, FanMonitor monitor) {
        this.reader = reader;
        this.monitor = monitor;
    }

    @GetMapping
    public List<FanReading> list() {
        return reader.getFanReadings();
    }

    @GetMapping("/{id}")
    public List<FanReading> byId(@PathVariable String id) {
        return reader.getFanReadings().stream().filter(f -> f.getId().equals(id)).collect(Collectors.toList());
    }
}
