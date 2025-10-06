package com.agora.monitoring.ui;

import com.agora.monitoring.alert.AlertService;
import com.agora.monitoring.config.ConfigService;
import com.agora.monitoring.monitor.FanMonitor;
import com.agora.monitoring.monitor.TemperatureMonitor;
import com.agora.monitoring.model.FanReading;
import com.agora.monitoring.model.TemperatureReading;
import com.agora.monitoring.sensor.RealFanReader;
import com.agora.monitoring.sensor.SimulatedFanReader;
import com.agora.monitoring.sensor.RealSensorReader;
import com.agora.monitoring.sensor.SimulatedSensorReader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MonitoringGui implements AlertService.AlertListener {
    private TemperatureMonitor tempMonitor;
    private final FanMonitor fanMonitor;
    private boolean forceRealSensors = false;
    private final ConfigService config;
    private final AlertService alertService;
    private final JFrame frame;

    private final DefaultTableModel tempModel = new DefaultTableModel(new String[]{"Id","Type","Value (°C)","When","Threshold","Source","Status"}, 0);
    private final DefaultTableModel fanModel = new DefaultTableModel(new String[]{"Id","Name","RPM","When","Threshold","Source","Status"}, 0);
    private JTextArea alertsArea = null;
    private final StringBuilder alertsBuffer = new StringBuilder();

    private final DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_TIME;
    private javax.swing.Timer swingTimer;
    private JTable tempTableField;
    private JTable fanTableField;

    public MonitoringGui(ConfigService config, AlertService alertService) {
        this(config, alertService, true);
    }

    /**
     * Create MonitoringGui but optionally skip UI initialization (useful for headless tests).
     */
    public MonitoringGui(ConfigService config, AlertService alertService, boolean initUi) {
        this.config = config;
        this.alertService = alertService;
    // Prefer real readers by default; they fall back to simulated data when hardware isn't available
    boolean forceReal = Boolean.getBoolean("monitoring.forceRealSensors");
    this.forceRealSensors = forceReal;
    this.tempMonitor = new TemperatureMonitor(new RealSensorReader(!forceReal), config, alertService);
    // Prefer a real reader where available; RealFanReader will fall back to simulation if needed
    this.fanMonitor = new FanMonitor(new RealFanReader(), config, alertService);
        if (initUi) {
            this.frame = new JFrame("System Monitoring GUI");
            initUi();
            frame.setMinimumSize(new Dimension(900, 500));
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            alertService.addListener(this);
        } else {
            // no UI in headless/test mode
            this.frame = null;
        }
    }

    private void initUi() {
        frame.getContentPane().setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();

    JTable tempTable = new JTable(tempModel);
    this.tempTableField = tempTable;
        // prefer horizontal scrolling so all columns are visible; set sensible widths
        tempTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tempTable.setPreferredScrollableViewportSize(new Dimension(800, 150));
        try {
            var cm = tempTable.getColumnModel();
            if (cm.getColumnCount() >= 7) {
                cm.getColumn(0).setPreferredWidth(80);  // Id
                cm.getColumn(1).setPreferredWidth(100); // Type
                cm.getColumn(2).setPreferredWidth(100); // Value
                cm.getColumn(3).setPreferredWidth(160); // When
                cm.getColumn(4).setPreferredWidth(80);  // Threshold
                cm.getColumn(5).setPreferredWidth(100); // Source
                cm.getColumn(6).setPreferredWidth(80);  // Status
            }
        } catch (Exception ignored) {}
        JScrollPane tempScroll = new JScrollPane(tempTable);
        JPanel tempPanel = new JPanel(new BorderLayout());
        tempPanel.add(tempScroll, BorderLayout.CENTER);

        JPanel tempControls = new JPanel();
        tempControls.add(new JLabel("Sensor ID:"));
        JTextField sensorId = new JTextField(10);
        tempControls.add(sensorId);
        tempControls.add(new JLabel("Max °C:"));
        JTextField maxVal = new JTextField(6);
        tempControls.add(maxVal);
        JButton setMax = new JButton("Set Max");
        setMax.addActionListener((ActionEvent e) -> {
            String id = sensorId.getText().trim();
            try {
                double v = Double.parseDouble(maxVal.getText().trim());
                if (!id.isEmpty()) {
                    config.setMaxThreshold(id, v);
                    config.save();
                    refreshOnce();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Valeur numérique attendue pour le seuil.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        tempControls.add(setMax);
        JCheckBox forceReal = new JCheckBox("Force Real Sensors");
        forceReal.setSelected(forceRealSensors);
        forceReal.addActionListener((ActionEvent e) -> {
            forceRealSensors = forceReal.isSelected();
            // recreate tempMonitor with or without fallback immediately
            try {
                this.tempMonitor = new TemperatureMonitor(new com.agora.monitoring.sensor.RealSensorReader(!forceRealSensors), config, alertService);
            } catch (Exception ex) {
                // fallback to previous monitor on error
            }
            // refresh UI immediately to reflect the new reader
            refreshOnce();
        });
        tempControls.add(forceReal);
        tempPanel.add(tempControls, BorderLayout.SOUTH);

        tabs.addTab("Temperatures", tempPanel);

    JTable fanTable = new JTable(fanModel);
    this.fanTableField = fanTable;
        fanTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        fanTable.setPreferredScrollableViewportSize(new Dimension(800, 150));
        try {
            var cmf = fanTable.getColumnModel();
            if (cmf.getColumnCount() >= 7) {
                cmf.getColumn(0).setPreferredWidth(80);  // Id
                cmf.getColumn(1).setPreferredWidth(120); // Name
                cmf.getColumn(2).setPreferredWidth(80);  // RPM
                cmf.getColumn(3).setPreferredWidth(160); // When
                cmf.getColumn(4).setPreferredWidth(80);  // Threshold
                cmf.getColumn(5).setPreferredWidth(100); // Source
                cmf.getColumn(6).setPreferredWidth(80);  // Status
            }
        } catch (Exception ignored) {}
        JScrollPane fanScroll = new JScrollPane(fanTable);
        JPanel fanPanel = new JPanel(new BorderLayout());
        fanPanel.add(fanScroll, BorderLayout.CENTER);

        JPanel fanControls = new JPanel();
        fanControls.add(new JLabel("Fan ID:"));
        JTextField fanId = new JTextField(10);
        fanControls.add(fanId);
        fanControls.add(new JLabel("Min RPM:"));
        JTextField fanMin = new JTextField(6);
        fanControls.add(fanMin);
        JButton setFan = new JButton("Set Min RPM");
        setFan.addActionListener((ActionEvent e) -> {
            String id = fanId.getText().trim();
            try {
                if (!id.isEmpty()) {
                    if (!fanMin.getText().trim().isEmpty()) {
                        double minv = Double.parseDouble(fanMin.getText().trim());
                        config.setMaxThreshold("fan:" + id + ":min", minv);
                    }
                    config.save();
                    refreshOnce();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Valeur numérique attendue pour les limites.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        fanControls.add(setFan);
        fanPanel.add(fanControls, BorderLayout.SOUTH);

        tabs.addTab("Fans", fanPanel);

    frame.getContentPane().add(tabs, BorderLayout.CENTER);

        alertsArea = new JTextArea(6, 60);
        alertsArea.setEditable(false);
    JPanel alertsPanel = new JPanel(new BorderLayout());
    alertsPanel.add(new JScrollPane(alertsArea), BorderLayout.CENTER);
    JButton clearAlerts = new JButton("Clear Alerts");
    clearAlerts.addActionListener((ActionEvent e) -> alertsArea.setText(""));
    JPanel alertsControls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    alertsControls.add(clearAlerts);
    alertsPanel.add(alertsControls, BorderLayout.SOUTH);
    frame.getContentPane().add(alertsPanel, BorderLayout.SOUTH);

        // renderer to highlight alert rows in red/pink
        javax.swing.table.DefaultTableCellRenderer alertRenderer = new javax.swing.table.DefaultTableCellRenderer() {
            private final javax.swing.border.Border alertBorder = javax.swing.BorderFactory.createLineBorder(Color.RED, 2);
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                try {
                    Object status = table.getModel().getValueAt(row, table.getColumnCount() - 1);
                    if (status != null && "ALERT".equalsIgnoreCase(status.toString())) {
                        c.setBackground(new Color(0xCC, 0x00, 0x00)); // stronger red
                        c.setForeground(Color.WHITE);
                        if (c instanceof JComponent) ((JComponent) c).setBorder(alertBorder);
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                        if (c instanceof JComponent) ((JComponent) c).setBorder(null);
                    }
                } catch (Exception e) {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        };
        tempTable.setDefaultRenderer(Object.class, alertRenderer);
        fanTable.setDefaultRenderer(Object.class, alertRenderer);

        // polling timer using Swing Timer so actions run on EDT
        swingTimer = new javax.swing.Timer(3000, (ActionEvent e) -> refreshOnce());
        swingTimer.setInitialDelay(0);
        swingTimer.start();
    }

    private void refreshOnce() {
        // temperatures
        List<TemperatureReading> temps;
        if (forceRealSensors) {
            // attempt to get real readings without fallback
            temps = new com.agora.monitoring.sensor.RealSensorReader(false).getTemperatureReadings();
        } else {
            temps = tempMonitor.pollOnce();
        }
        tempModel.setRowCount(0);
        for (TemperatureReading t : temps) {
            Double thr = config.getMaxThreshold(t.getId());
            String status = (thr != null && t.getValueC() > thr) ? "ALERT" : "OK";
            String tSource = (t.getSource() == null ? "UNKNOWN" : t.getSource());
            String valueStr = Double.isNaN(t.getValueC()) ? "N/A" : String.format("%.2f", t.getValueC());
            tempModel.addRow(new Object[]{t.getId(), t.getType(), valueStr, t.getTimestamp().toString(), thr, tSource, status});
        }
        if (tempTableField != null) {
            tempTableField.revalidate();
            tempTableField.repaint();
        }

        // fans
        List<FanReading> fans = fanMonitor.pollOnce();
        fanModel.setRowCount(0);
        for (FanReading f : fans) {
            Double min = config.getMaxThreshold("fan:" + f.getId() + ":min");
            String status = (min != null && f.getRpm() < min) ? "ALERT" : "OK";
            String fSource = (f.getSource() == null ? "UNKNOWN" : f.getSource());
            fanModel.addRow(new Object[]{f.getId(), f.getName(), f.getRpm(), f.getTimestamp().toString(), min, fSource, status});
        }
        if (fanTableField != null) {
            fanTableField.revalidate();
            fanTableField.repaint();
        }
    }

    @Override
    public void onAlert(String sensorId, double value, double threshold) {
        SwingUtilities.invokeLater(() -> {
            String line = String.format("ALERTE: %s valeur=%.2f seuil=%.2f\n", sensorId, value, threshold);
            if (alertsArea != null) {
                alertsArea.append(line);
            } else {
                synchronized (alertsBuffer) {
                    alertsBuffer.append(line);
                }
            }
            // optionally flash window
            if (frame != null) frame.toFront();
        });
    }

    public static void startGui(File configFile) {
        ConfigService config = new ConfigService(configFile);
        AlertService alert = new AlertService();
        MonitoringGui g = new MonitoringGui(config, alert);
        SwingUtilities.invokeLater(() -> g.setVisible(true));
    }

    public void setVisible(boolean v) {
        if (frame != null) {
            frame.setVisible(v);
            if (v && swingTimer != null && !swingTimer.isRunning()) swingTimer.start();
            if (!v && swingTimer != null && swingTimer.isRunning()) swingTimer.stop();
        }
    }

    public void toFront() {
        if (frame != null) frame.toFront();
    }

    public void dispose() {
        if (swingTimer != null) swingTimer.stop();
        if (frame != null) frame.dispose();
    }
}
