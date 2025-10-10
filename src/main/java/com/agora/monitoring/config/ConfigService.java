package com.agora.monitoring.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ConfigService {
    private final File file;
    private final Gson gson = new Gson();
    private Map<String, Double> maxThresholds = new HashMap<>();

    public ConfigService(File file) {
        this.file = file;
        load();
    }

    public synchronized void load() {
        try {
            if (!file.exists()) return;
            try (FileReader fr = new FileReader(file)) {
                Type t = new TypeToken<Map<String, Double>>(){}.getType();
                Map<String, Double> read = gson.fromJson(fr, t);
                if (read != null) maxThresholds = read;
            }
        } catch (Exception e) {
            // ignore and keep defaults
        }
    }

    public synchronized void save() {
        try (FileWriter fw = new FileWriter(file)) {
            gson.toJson(maxThresholds, fw);
        } catch (Exception e) {
            // ignore
        }
    }

    public synchronized void setMaxThreshold(String sensorId, double maxC) {
        maxThresholds.put(sensorId, maxC);
    }

    public synchronized Double getMaxThreshold(String sensorId) {
        return maxThresholds.get(sensorId);
    }

    /**
     * Return a copy of all configured thresholds.
     */
    public synchronized java.util.Map<String, Double> getAll() {
        return new java.util.HashMap<>(maxThresholds);
    }
}
