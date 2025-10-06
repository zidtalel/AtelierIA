package com.agora.monitoring.sensor;

import com.agora.monitoring.model.FanReading;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Attempts to query Win32_Fan via the wmic command on Windows to get fan speeds.
 * Falls back to simulated reader if WMIC is unavailable or returns no data.
 */
public class WmiFanReader implements FanReader {
    private final SimulatedFanReader fallback = new SimulatedFanReader();

    @Override
    public List<FanReading> getFanReadings() {
        String os = System.getProperty("os.name", "").toLowerCase();
        if (!os.contains("win")) {
            return fallback.getFanReadings();
        }
        try {
            ProcessBuilder pb = new ProcessBuilder("wmic", "path", "Win32_Fan", "get", "DeviceID,Name,DesiredSpeed,Speed", "/format:csv");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            boolean finished = p.waitFor(2, TimeUnit.SECONDS);
            if (!finished) {
                p.destroyForcibly();
                return fallback.getFanReadings();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            List<FanReading> out = new ArrayList<>();
            Pattern numPattern = Pattern.compile("(-?\\d+)");
            Instant now = Instant.now();
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.toLowerCase().contains("deviceid") || line.toLowerCase().contains("node")) continue;
                // attempt to parse CSV: Node,DeviceID,Name,DesiredSpeed,Speed
                String[] parts = line.split(",");
                String id = null;
                Integer speed = null;
                if (parts.length >= 3) {
                    // try to find numeric speed somewhere
                    for (int i = 0; i < parts.length; i++) {
                        String pstr = parts[i].trim();
                        if (pstr.isEmpty()) continue;
                        Matcher m = numPattern.matcher(pstr);
                        if (m.find()) {
                            try {
                                speed = Integer.parseInt(m.group(1));
                                break;
                            } catch (NumberFormatException ignored) {}
                        }
                    }
                    // pick a string-like token for id
                    for (String pstr : parts) {
                        pstr = pstr.trim();
                        if (pstr.isEmpty()) continue;
                        if (pstr.matches(".*[A-Za-z].*")) {
                            id = pstr.replaceAll("\"", "");
                            break;
                        }
                    }
                }
                if (speed != null && speed > 0) {
                    String rid = (id == null ? "fan0" : id);
                    out.add(new FanReading(rid, rid, speed, now, "WMI"));
                }
            }
            if (out.isEmpty()) {
                return fallback.getFanReadings();
            }
            return out;
        } catch (Throwable t) {
            return fallback.getFanReadings();
        }
    }
}
