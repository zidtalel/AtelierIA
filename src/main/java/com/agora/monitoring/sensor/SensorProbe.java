package com.agora.monitoring.sensor;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Sensors;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.List;

public class SensorProbe {
    public static void main(String[] args) throws Exception {
        System.out.println("=== SensorProbe ===");
        String os = System.getProperty("os.name", "");
        System.out.println("OS: " + os);

        // WMIC/PowerShell checks removed — this tool no longer executes external WMI commands.
        if (os.toLowerCase().contains("win")) {
            System.out.println("WMIC/PowerShell checks disabled by configuration; WMI readers are no-ops.");
        } else {
            System.out.println("WMIC/PowerShell checks skipped (not Windows)");
        }

        System.out.println("-- WMI readers removed --");
        System.out.println("WMI-based readers (WmiSensorReader/WmiFanReader) were removed from the codebase.");
        System.out.println("Use OSHI or other platform-specific readers instead.");

        System.out.println("-- Using OSHI Sensors --");
        try {
            SystemInfo si = new SystemInfo();
            HardwareAbstractionLayer hal = si.getHardware();
            Sensors sensors = hal.getSensors();
            double cpuTemp = sensors.getCpuTemperature();
            System.out.println("OSHI: cpuTemp=" + cpuTemp);
            int[] fanSpeeds = sensors.getFanSpeeds();
            if (fanSpeeds == null) System.out.println("OSHI: fans=null");
            else System.out.println("OSHI: fans.len=" + fanSpeeds.length + " values=" + java.util.Arrays.toString(fanSpeeds));
        } catch (Throwable t) {
            System.out.println("OSHI sensors failed: " + t.getMessage());
        }

        System.out.println("=== SensorProbe done: " + Instant.now() + " ===");
    }
}
