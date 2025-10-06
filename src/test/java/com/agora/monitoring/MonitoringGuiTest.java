package com.agora.monitoring;

import com.agora.monitoring.alert.AlertService;
import com.agora.monitoring.config.ConfigService;
import com.agora.monitoring.ui.MonitoringGui;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class MonitoringGuiTest {

    @Test
    public void testGuiTableModelsHaveStatus() throws Exception {
        // run headless to avoid display issues
        System.setProperty("java.awt.headless", "true");

        ConfigService config = new ConfigService(new File("target/test-config.json"));
        AlertService alert = new AlertService();
    // create GUI object without initializing Swing UI to avoid HeadlessException
    MonitoringGui gui = new MonitoringGui(config, alert, false);

        // call private refreshOnce() via reflection
        Method refresh = MonitoringGui.class.getDeclaredMethod("refreshOnce");
        refresh.setAccessible(true);
        refresh.invoke(gui);

        // access private table models
        Field tempModelField = MonitoringGui.class.getDeclaredField("tempModel");
        tempModelField.setAccessible(true);
        DefaultTableModel tempModel = (DefaultTableModel) tempModelField.get(gui);

        Field fanModelField = MonitoringGui.class.getDeclaredField("fanModel");
        fanModelField.setAccessible(true);
        DefaultTableModel fanModel = (DefaultTableModel) fanModelField.get(gui);

    // Expect Status as last column and Source right before it
    assertTrue(tempModel.getColumnCount() >= 7, "Temperature table should have a Source and Status column");
    assertTrue(fanModel.getColumnCount() >= 7, "Fan table should have a Source and Status column");

        assertTrue(tempModel.getRowCount() > 0, "Temperature table should have rows after refresh");
        assertTrue(fanModel.getRowCount() > 0, "Fan table should have rows after refresh");

        // check status values present in at least one row
        boolean tempHasStatus = false;
        for (int r = 0; r < tempModel.getRowCount(); r++) {
            Object s = tempModel.getValueAt(r, tempModel.getColumnCount() - 1);
            if (s != null && ("OK".equals(s) || "ALERT".equals(s))) {
                tempHasStatus = true;
                break;
            }
        }
        boolean fanHasStatus = false;
        for (int r = 0; r < fanModel.getRowCount(); r++) {
            Object s = fanModel.getValueAt(r, fanModel.getColumnCount() - 1);
            if (s != null && ("OK".equals(s) || "ALERT".equals(s))) {
                fanHasStatus = true;
                break;
            }
        }

        assertTrue(tempHasStatus, "Temperature table rows should contain OK/ALERT status");
        assertTrue(fanHasStatus, "Fan table rows should contain OK/ALERT status");

            // check Source column (second-to-last column) contains SIMULATED or REAL in at least one row
            int tempSourceIdx = tempModel.getColumnCount() - 2;
            boolean tempHasSource = false;
            for (int r = 0; r < tempModel.getRowCount(); r++) {
                Object src = tempModel.getValueAt(r, tempSourceIdx);
                if (src != null) {
                    String s = src.toString();
                    if ("SIMULATED".equalsIgnoreCase(s) || "REAL".equalsIgnoreCase(s) || "UNKNOWN".equalsIgnoreCase(s)) {
                        tempHasSource = true;
                        break;
                    }
                }
            }

            int fanSourceIdx = fanModel.getColumnCount() - 2;
            boolean fanHasSource = false;
            for (int r = 0; r < fanModel.getRowCount(); r++) {
                Object src = fanModel.getValueAt(r, fanSourceIdx);
                if (src != null) {
                    String s = src.toString();
                    if ("SIMULATED".equalsIgnoreCase(s) || "REAL".equalsIgnoreCase(s) || "UNKNOWN".equalsIgnoreCase(s)) {
                        fanHasSource = true;
                        break;
                    }
                }
            }

            assertTrue(tempHasSource, "Temperature table should contain a Source value (SIMULATED/REAL)");
            assertTrue(fanHasSource, "Fan table should contain a Source value (SIMULATED/REAL)");

        // dispose GUI
        SwingUtilities.invokeLater(() -> gui.dispose());
    }
}
