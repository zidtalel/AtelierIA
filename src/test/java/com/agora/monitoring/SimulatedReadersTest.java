package com.agora.monitoring;

import com.agora.monitoring.sensor.SimulatedFanReader;
import com.agora.monitoring.sensor.SimulatedSensorReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SimulatedReadersTest {

    @Test
    public void testTemperatureReaderReturnsReadings() {
        SimulatedSensorReader r = new SimulatedSensorReader();
        var list = r.getTemperatureReadings();
        assertNotNull(list);
        assertTrue(list.size() >= 1);
        assertNotNull(list.get(0).getId());
    }

    @Test
    public void testFanReaderReturnsReadings() {
        SimulatedFanReader r = new SimulatedFanReader();
        var list = r.getFanReadings();
        assertNotNull(list);
        assertTrue(list.size() >= 1);
        assertTrue(list.get(0).getRpm() >= 0);
    }
}
