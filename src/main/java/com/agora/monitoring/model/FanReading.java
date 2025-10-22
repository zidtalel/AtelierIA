package com.agora.monitoring.model;

import java.time.Instant;

public class FanReading {
    private final String id;
    private final String name;
    private final int rpm;
    private final String source;
    private final Instant timestamp;

    public FanReading(String id, String name, int rpm, Instant timestamp) {
        this(id, name, rpm, timestamp, "SIMULATED");
    }

    public FanReading(String id, String name, int rpm, Instant timestamp, String source) {
        this.id = id;
        this.name = name;
        this.rpm = rpm;
        this.timestamp = timestamp;
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRpm() {
        return rpm;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getSource() {
        return source;
    }
}
