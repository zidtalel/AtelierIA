package com.agora.monitoring.alert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AlertService {
    private static final Logger logger = LoggerFactory.getLogger(AlertService.class);

    public interface AlertListener {
        void onAlert(String sensorId, double value, double threshold);
    }

    private final List<AlertListener> listeners = new CopyOnWriteArrayList<>();
    // store recent alerts (simple in-memory bounded buffer)
    private final java.util.Deque<AlertRecord> recent = new java.util.ArrayDeque<>();
    private final int maxStored = 200;

    public static class AlertRecord {
        public final String sensorId;
        public final double value;
        public final double threshold;
        public final java.time.Instant timestamp;

        public AlertRecord(String sensorId, double value, double threshold) {
            this.sensorId = sensorId;
            this.value = value;
            this.threshold = threshold;
            this.timestamp = java.time.Instant.now();
        }
    }

    public void alert(String sensorId, double value, double threshold) {
        String msg = String.format("ALERTE: capteur=%s valeur=%.2f°C seuil=%.2f°C", sensorId, value, threshold);
        // log and (in real app) show UI notification
        logger.warn(msg);
        // store
        synchronized (recent) {
            recent.addFirst(new AlertRecord(sensorId, value, threshold));
            while (recent.size() > maxStored) recent.removeLast();
        }
        for (AlertListener l : listeners) {
            try {
                l.onAlert(sensorId, value, threshold);
            } catch (Exception e) {
                logger.error("Error in alert listener", e);
            }
        }
    }

    public java.util.List<AlertRecord> getRecentAlerts() {
        synchronized (recent) {
            return new java.util.ArrayList<>(recent);
        }
    }

    public void clearRecent() {
        synchronized (recent) {
            recent.clear();
        }
    }

    public void addListener(AlertListener l) {
        if (l != null) listeners.add(l);
    }

    public void removeListener(AlertListener l) {
        if (l != null) listeners.remove(l);
    }
}
