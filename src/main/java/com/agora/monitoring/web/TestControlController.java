package com.agora.monitoring.web;

import com.agora.monitoring.monitor.TemperatureMonitor;
import com.agora.monitoring.monitor.FanMonitor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/control")
public class TestControlController {

    private final TemperatureMonitor tempMonitor;
    private final FanMonitor fanMonitor;

    public TestControlController(TemperatureMonitor tempMonitor, FanMonitor fanMonitor) {
        this.tempMonitor = tempMonitor;
        this.fanMonitor = fanMonitor;
    }

    @PostMapping("/poll/temps")
    public ResponseEntity<?> pollTemps() {
        try {
            tempMonitor.pollOnce();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/poll/fans")
    public ResponseEntity<?> pollFans() {
        try {
            fanMonitor.pollOnce();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
