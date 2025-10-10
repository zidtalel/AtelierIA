package com.agora.monitoring.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }

    @Bean
    public org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler s = new org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler();
        s.setPoolSize(4);
        s.setThreadNamePrefix("websocket-scheduler-");
        s.initialize();
        return s;
    }

    @Bean
    public ReadingPublisher readingPublisher(org.springframework.messaging.simp.SimpMessagingTemplate template,
                                              com.agora.monitoring.alert.AlertService alertService,
                                              com.agora.monitoring.config.ConfigService configService) {
        ReadingPublisher p = new ReadingPublisher(template, configService);
        p.setTemplate(template);

        // forward alerts to /topic/alerts so UI can display them
        if (alertService != null) {
            alertService.addListener((sensorId, value, threshold) -> {
                try {
                    java.util.Map<String, Object> payload = new java.util.HashMap<>();
                    payload.put("sensorId", sensorId);
                    payload.put("value", value);
                    payload.put("threshold", threshold);
                    payload.put("timestamp", java.time.Instant.now().toString());
                    // best-effort kind detection: fans are prefixed with fan_
                    payload.put("kind", sensorId != null && sensorId.startsWith("fan") ? "fan" : "temp");
                    template.convertAndSend("/topic/alerts", payload);
                } catch (Exception e) {
                    // ignore
                }
            });
        }
        return p;
    }
}
