package com.agora.monitoring.ui;

import java.io.File;

public class GuiLauncher {
    public static void main(String[] args) {
        File configFile = new File("config.json");
        if (args.length > 0) configFile = new File(args[0]);
        MonitoringGui.startGui(configFile);
    }
}
