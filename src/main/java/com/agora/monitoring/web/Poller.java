package com.agora.monitoring.web;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Poller {

    private final com.agora.monitoring.monitor.TemperatureMonitor monitor;
    private final ReadingPublisher publisher;
    private final com.agora.monitoring.monitor.FanMonitor fanMonitor;

    public Poller(com.agora.monitoring.monitor.TemperatureMonitor monitor, ReadingPublisher publisher,
                  com.agora.monitoring.monitor.FanMonitor fanMonitor) {
        this.monitor = monitor;
        this.publisher = publisher;
        this.fanMonitor = fanMonitor;
    }

    @Scheduled(fixedDelayString = "${monitor.poll.interval:5000}")
    public void poll() {
        try {
            var readings = monitor.pollOnce();
            publisher.publishTemperatureReadings(readings);
            // also poll fan readings and publish them
            try {
                var fans = fanMonitor.pollOnce();
                publisher.publishFanReadings(fans);
            } catch (Exception e) {
                // ignore fan errors
            }
        } catch (Exception e) {
            // log if necessary
        }
    }
}
