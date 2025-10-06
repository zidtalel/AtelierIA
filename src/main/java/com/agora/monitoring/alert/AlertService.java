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

    public void alert(String sensorId, double value, double threshold) {
        String msg = String.format("ALERTE: capteur=%s valeur=%.2f°C seuil=%.2f°C", sensorId, value, threshold);
        // log and (in real app) show UI notification
        logger.warn(msg);
        for (AlertListener l : listeners) {
            try {
                l.onAlert(sensorId, value, threshold);
            } catch (Exception e) {
                logger.error("Error in alert listener", e);
            }
        }
    }

    public void addListener(AlertListener l) {
        if (l != null) listeners.add(l);
    }

    public void removeListener(AlertListener l) {
        if (l != null) listeners.remove(l);
    }
}
