package com.agora.monitoring.model;

import java.time.Instant;

public class TemperatureReading {
    private final String id;
    private final String type;
    private final double valueC;
    private final String source;
    private final Instant timestamp;

    public TemperatureReading(String id, String type, double valueC, Instant timestamp) {
        this(id, type, valueC, timestamp, "SIMULATED");
    }

    public TemperatureReading(String id, String type, double valueC, Instant timestamp, String source) {
        this.id = id;
        this.type = type;
        this.valueC = valueC;
        this.timestamp = timestamp;
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public double getValueC() {
        return valueC;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getSource() {
        return source;
    }
}
