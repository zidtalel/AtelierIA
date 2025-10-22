package com.agora.monitoring.web;

import com.agora.monitoring.sensor.SensorReader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {

    private final SensorReader reader;
    private final com.agora.monitoring.sensor.FanReader fanReader;
    private final com.agora.monitoring.config.ConfigService configService;
    private final com.agora.monitoring.alert.AlertService alertService;

    public MainController(SensorReader reader, com.agora.monitoring.sensor.FanReader fanReader,
                          com.agora.monitoring.config.ConfigService configService,
                          com.agora.monitoring.alert.AlertService alertService) {
        this.reader = reader;
        this.fanReader = fanReader;
        this.configService = configService;
        this.alertService = alertService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        var temps = reader.getTemperatureReadings();
        var fans = fanReader.getFanReadings();
        // enrich with thresholds and status
        model.addAttribute("sensors", temps);
        model.addAttribute("fans", fans);
        try {
            model.addAttribute("alerts", alertService.getRecentAlerts());
        } catch (Exception e) {
            model.addAttribute("alerts", java.util.Collections.emptyList());
        }
        try {
            model.addAttribute("thresholds", configService.getAll());
        } catch (Exception e) {
            model.addAttribute("thresholds", java.util.Collections.emptyMap());
        }
        return "dashboard";
    }
}
