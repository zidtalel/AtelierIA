package com.agora.monitoring;

import com.agora.monitoring.model.TemperatureReading;
import com.agora.monitoring.web.ReadingPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReadingPublisherTest {

    static class FakeTemplate extends SimpMessagingTemplate {
        public Object lastPayload;
        public String lastDestination;

        public FakeTemplate() {
            super(new org.springframework.messaging.MessageChannel() {
                @Override
                public boolean send(org.springframework.messaging.Message<?> message) {
                    return true;
                }

                @Override
                public boolean send(org.springframework.messaging.Message<?> message, long timeout) {
                    return true;
                }
            });
        }

        @Override
        public void convertAndSend(String destination, Object payload) {
            this.lastDestination = destination;
            this.lastPayload = payload;
        }
    }

    @Test
    public void publishTemperatureReadings_noTemplateLogs() {
        ReadingPublisher p = new ReadingPublisher();
        // no exception expected
        p.publishTemperatureReadings(List.of());
    }

    @Test
    public void publishTemperatureReadings_withTemplateSends() {
        FakeTemplate t = new FakeTemplate();
        ReadingPublisher p = new ReadingPublisher(t);
        TemperatureReading r = new TemperatureReading("cpu0","CPU",42.0, Instant.now(), "SIM");
        p.publishTemperatureReadings(List.of(r));
        assertEquals("/topic/readings", t.lastDestination);
        assertNotNull(t.lastPayload);
    }
}
