package com.agora.monitoring.sensor;

import com.agora.monitoring.model.FanReading;

import java.util.List;

/**
 * Abstraction for a fan reader implementation.
 */
public interface FanReader {
    List<FanReading> getFanReadings();
}
