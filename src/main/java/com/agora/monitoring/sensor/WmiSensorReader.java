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
 * Attempts to query Windows WMI (MSAcpi_ThermalZoneTemperature) via the wmic command
 * as a best-effort method to surface real temperature values on Windows machines.
 * Falls back to simulated values if WMIC isn't available or returns nothing.
 */
public class WmiSensorReader implements SensorReader {
    private final SimulatedSensorReader fallback = new SimulatedSensorReader();

    @Override
    public List<TemperatureReading> getTemperatureReadings() {
        String os = System.getProperty("os.name", "").toLowerCase();
        if (!os.contains("win")) {
            return fallback.getTemperatureReadings();
        }

        try {
            // Run wmic query for thermal zones. Use /format:csv for more predictable output.
            ProcessBuilder pb = new ProcessBuilder("wmic", "/namespace:\\root\\wmi",
                    "PATH", "MSAcpi_ThermalZoneTemperature", "get", "CurrentTemperature,InstanceName", "/format:csv");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            boolean finished = p.waitFor(2, TimeUnit.SECONDS);
            if (!finished) {
                p.destroyForcibly();
                return fallback.getTemperatureReadings();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            List<TemperatureReading> out = new ArrayList<>();
            String line;
            // We expect CSV lines like: Node,CurrentTemperature,InstanceName  or values. We'll extract numeric tokens.
            Pattern numPattern = Pattern.compile("(-?\\d+)");
            Instant now = Instant.now();
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                // skip header lines containing words
                if (line.toLowerCase().contains("currenttemperature") || line.toLowerCase().contains("node") || line.toLowerCase().contains("instancename")) continue;
                // find numeric token which is CurrentTemperature in tenths of Kelvin
                Matcher m = numPattern.matcher(line);
                Double tempC = null;
                String id = null;
                while (m.find()) {
                    String tok = m.group(1);
                    // Heuristic: the first integer we find is CurrentTemperature
                    try {
                        int val = Integer.parseInt(tok);
                        // value 0 or negative likely not valid; still process
                        double c = val / 10.0 - 273.15;
                        tempC = c;
                        break;
                    } catch (NumberFormatException ignored) {}
                }
                // Try also to extract an instance name (alphanumeric) by splitting by commas
                String[] parts = line.split(",");
                for (String pstr : parts) {
                    pstr = pstr.trim();
                    if (pstr.isEmpty()) continue;
                    // if it contains letters, treat it as instance name
                    if (pstr.matches(".*[A-Za-z].*")) {
                        id = pstr.replaceAll("\"", "");
                        break;
                    }
                }
                if (tempC != null) {
                    String rid = (id == null ? "cpu0" : id);
                    out.add(new TemperatureReading(rid, "WMI", tempC, now, "WMI"));
                }
            }
            if (out.isEmpty()) {
                return fallback.getTemperatureReadings();
            }
            return out;
        } catch (Throwable t) {
            return fallback.getTemperatureReadings();
        }
    }
}
