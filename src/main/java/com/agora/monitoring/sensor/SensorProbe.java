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

        // Try WMIC thermal
        if (os.toLowerCase().contains("win")) {
            System.out.println("-- Attempting WMIC thermal query --");
            try {
                ProcessBuilder pb = new ProcessBuilder("wmic", "/namespace:\\root\\wmi", "PATH", "MSAcpi_ThermalZoneTemperature", "get", "CurrentTemperature,InstanceName", "/format:csv");
                pb.redirectErrorStream(true);
                Process p = pb.start();
                boolean finished = p.waitFor(3_000, java.util.concurrent.TimeUnit.MILLISECONDS);
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
                if (!finished) System.out.println("(WMIC command timed out)");
            } catch (Throwable t) {
                System.out.println("WMIC thermal query failed: " + t.getMessage());
            }

            System.out.println("-- Attempting PowerShell CIM query --");
            try {
                ProcessBuilder pb2 = new ProcessBuilder("powershell", "-Command",
                        "Get-CimInstance -Namespace root/wmi -ClassName MSAcpi_ThermalZoneTemperature | Select-Object InstanceName, CurrentTemperature | Format-List");
                pb2.redirectErrorStream(true);
                Process p2 = pb2.start();
                boolean finished2 = p2.waitFor(3_000, java.util.concurrent.TimeUnit.MILLISECONDS);
                BufferedReader br2 = new BufferedReader(new InputStreamReader(p2.getInputStream()));
                String line2;
                while ((line2 = br2.readLine()) != null) {
                    System.out.println(line2);
                }
                if (!finished2) System.out.println("(PowerShell query timed out)");
            } catch (Throwable t) {
                System.out.println("PowerShell CIM query failed: " + t.getMessage());
            }
        } else {
            System.out.println("WMIC/PowerShell checks skipped (not Windows)");
        }

        System.out.println("-- Using WmiSensorReader --");
        try {
            WmiSensorReader wmi = new WmiSensorReader();
            List<com.agora.monitoring.model.TemperatureReading> temps = wmi.getTemperatureReadings();
            for (com.agora.monitoring.model.TemperatureReading tr : temps) {
                System.out.printf("WMI-> %s (%s): %s°C source=%s timestamp=%s\n", tr.getId(), tr.getType(), tr.getValueC(), tr.getSource(), tr.getTimestamp());
            }
        } catch (Throwable t) {
            System.out.println("WmiSensorReader failed: " + t.getMessage());
        }

        System.out.println("-- Using WmiFanReader --");
        try {
            WmiFanReader wfr = new WmiFanReader();
            List<com.agora.monitoring.model.FanReading> fans = wfr.getFanReadings();
            for (com.agora.monitoring.model.FanReading fr : fans) {
                System.out.printf("WMI FAN-> %s (%s): %d rpm source=%s timestamp=%s\n", fr.getId(), fr.getName(), fr.getRpm(), fr.getSource(), fr.getTimestamp());
            }
        } catch (Throwable t) {
            System.out.println("WmiFanReader failed: " + t.getMessage());
        }

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
