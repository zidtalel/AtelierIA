package com.agora.monitoring;

import com.agora.monitoring.config.ConfigService;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigServiceTest {

    @Test
    public void testSetGetAndPersistence() throws Exception {
        File tmp = File.createTempFile("cfg", ".json");
        if (tmp.exists()) tmp.delete();
        ConfigService cfg = new ConfigService(tmp);
        assertNull(cfg.getMaxThreshold("cpu0"));
        cfg.setMaxThreshold("cpu0", 42.5);
        assertEquals(42.5, cfg.getMaxThreshold("cpu0"));
        cfg.save();

        ConfigService cfg2 = new ConfigService(tmp);
        assertEquals(42.5, cfg2.getMaxThreshold("cpu0"));
        tmp.delete();
    }
}
