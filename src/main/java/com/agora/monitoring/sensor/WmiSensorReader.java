package com.agora.monitoring.sensor;

import com.agora.monitoring.model.TemperatureReading;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Windows-specific reader (WMI) — external querying removed.
 * This class previously attempted to query platform WMI for temperature values;
 * that behavior has been disabled: the reader now returns an empty result.
 */
public class WmiSensorReader implements SensorReader {
    /**
     * WMI usage removed. This reader no longer executes external 'wmic' calls.
     * Returning an empty list indicates WMI-based readings are not available.
     */
    @Override
    public List<TemperatureReading> getTemperatureReadings() {
        return new ArrayList<>();
    }
}
