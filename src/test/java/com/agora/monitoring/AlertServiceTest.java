package com.agora.monitoring;

import com.agora.monitoring.alert.AlertService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AlertServiceTest {

    @Test
    public void testAlertStoresAndNotifies() {
        AlertService svc = new AlertService();
        TestListener l = new TestListener();
        svc.addListener(l);

        svc.alert("cpu0", 100.0, 75.0);

        List<AlertService.AlertRecord> recent = svc.getRecentAlerts();
        assertFalse(recent.isEmpty());
        AlertService.AlertRecord r = recent.get(0);
        assertEquals("cpu0", r.sensorId);

        assertTrue(l.received);

        svc.clearRecent();
        assertTrue(svc.getRecentAlerts().isEmpty());
    }

    static class TestListener implements AlertService.AlertListener {
        boolean received = false;

        @Override
        public void onAlert(String sensorId, double value, double threshold) {
            received = true;
        }
    }
}
