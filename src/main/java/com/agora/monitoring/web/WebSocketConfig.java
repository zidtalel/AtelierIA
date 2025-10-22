package com.agora.monitoring.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.support.ExecutorSubscribableChannel;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Use the simple broker WITHOUT task scheduler to avoid MessageBroker-X thread leaks
        // Rely on SockJS heartbeat instead (configured in registerStompEndpoints)
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Primary TaskScheduler bean used by all @Scheduled tasks and SimpleBrokerMessageHandler.
     * Configured as daemon with aggressive shutdown to prevent thread leaks.
     */
    @Bean(name = "taskScheduler", destroyMethod = "shutdown")
    @org.springframework.context.annotation.Primary
    public org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler taskScheduler() {
        org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler s = new org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler();
        s.setPoolSize(4);
        s.setThreadNamePrefix("app-scheduler-");
        s.setDaemon(true);
        s.setWaitForTasksToCompleteOnShutdown(false);  // Ne PAS attendre les tâches
        s.setAwaitTerminationSeconds(0);  // Fermeture immédiate
        s.setRemoveOnCancelPolicy(true);
        s.initialize();
        return s;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS()
                .setSessionCookieNeeded(false)
                .setHeartbeatTime(10000)          // Send heartbeat every 10s
                .setDisconnectDelay(5000)         // Close session 5s after disconnect
                .setHttpMessageCacheSize(1000)
                .setStreamBytesLimit(524288);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // We provide explicit executor/channel beans (inboundChannelExecutor + clientInboundChannel)
        // so avoid creating an additional executor via the registration API which would be unmanaged.
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        // Outbound channel executor is provided via outboundChannelExecutor + clientOutboundChannel beans.
    }

    // Executors used by the STOMP client channels. Creating these beans with the
    // conventional channel bean names ensures Spring will use them for the
    // clientInboundChannel/clientOutboundChannel and we can shut them down on exit.
    @Bean
    public ThreadPoolTaskExecutor inboundChannelExecutor() {
        ThreadPoolTaskExecutor t = new ThreadPoolTaskExecutor();
        t.setCorePoolSize(4);
        t.setMaxPoolSize(8);
        t.setQueueCapacity(100);
        t.setThreadNamePrefix("clientInboundChannel-");
        // make threads daemon so Tomcat won't report them as webapp-created non-daemon threads
        t.setDaemon(true);
        // allow core threads to time out quickly when idle
        t.setAllowCoreThreadTimeOut(true);
        t.setKeepAliveSeconds(1);
        // ensure the executor waits for tasks on shutdown and give them a short grace period
        t.setWaitForTasksToCompleteOnShutdown(true);
        t.setAwaitTerminationSeconds(5);
        t.initialize();
        return t;
    }

    @Bean(name = "clientInboundChannel")
    public ExecutorSubscribableChannel clientInboundChannel(ThreadPoolTaskExecutor inboundChannelExecutor) {
        // Ensure the channel bean depends on the executor bean so Spring destroys the
        // channel before the executor: this guarantees the channel will stop sending
        // tasks before the executor is shut down, avoiding TaskRejectedException.
        return new ExecutorSubscribableChannel(inboundChannelExecutor);
    }

    @Bean
    public ThreadPoolTaskExecutor outboundChannelExecutor() {
        ThreadPoolTaskExecutor t = new ThreadPoolTaskExecutor();
        t.setCorePoolSize(4);
        t.setMaxPoolSize(8);
        t.setQueueCapacity(100);
        t.setThreadNamePrefix("clientOutboundChannel-");
        t.setDaemon(true);
        t.setAllowCoreThreadTimeOut(true);
        t.setKeepAliveSeconds(1);
        t.setWaitForTasksToCompleteOnShutdown(true);
        t.setAwaitTerminationSeconds(5);
        t.initialize();
        return t;
    }

    @Bean(name = "clientOutboundChannel")
    public ExecutorSubscribableChannel clientOutboundChannel(ThreadPoolTaskExecutor outboundChannelExecutor) {
        return new ExecutorSubscribableChannel(outboundChannelExecutor);
    }

    // Do not define beans named clientInboundChannel/clientOutboundChannel directly to
    // avoid conflicting with Spring's DelegatingWebSocketMessageBrokerConfiguration.
    // We instead try to bind our executors via ChannelRegistration above.

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
