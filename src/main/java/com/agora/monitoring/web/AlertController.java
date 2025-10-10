package com.agora.monitoring.web;

import com.agora.monitoring.alert.AlertService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    public List<AlertService.AlertRecord> recent() {
        return alertService.getRecentAlerts();
    }

    @org.springframework.web.bind.annotation.PostMapping("/clear")
    public void clear() {
        alertService.clearRecent();
    }
}
