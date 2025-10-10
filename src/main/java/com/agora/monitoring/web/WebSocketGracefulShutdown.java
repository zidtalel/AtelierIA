package com.agora.monitoring.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Ensure WebSocket broker and scheduler threads are cleanly stopped when the Spring context closes.
 * This reduces Tomcat warnings about threads not stopped (MessageBroker-*, clientInboundChannel-*, ...).
 */
@Component
public class WebSocketGracefulShutdown implements ApplicationListener<ContextClosedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketGracefulShutdown.class);

    private final ApplicationContext ctx;

    public WebSocketGracefulShutdown(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        // Try to stop any SimpleBrokerMessageHandler beans if present
        try {
            Class<?> brokerClass = null;
            try {
                brokerClass = Class.forName("org.springframework.messaging.simp.broker.SimpleBrokerMessageHandler");
            } catch (ClassNotFoundException e) {
                // not present
            }

            if (brokerClass != null) {
                Map<String, ?> brokers = ctx.getBeansOfType((Class) brokerClass);
                for (Object b : brokers.values()) {
                    try {
                        logger.info("Stopping SimpleBrokerMessageHandler bean {}",(b).getClass().getName());
                        brokerClass.getMethod("stop").invoke(b);
                    } catch (Exception e) {
                        logger.warn("Failed to stop SimpleBrokerMessageHandler bean cleanly", e);
                    }
                }
            }
        } catch (Throwable t) {
            logger.debug("No SimpleBrokerMessageHandler available or failed to stop", t);
        }

        // Shutdown TaskScheduler if available
        try {
            Map<String, ThreadPoolTaskScheduler> schedulers = ctx.getBeansOfType(ThreadPoolTaskScheduler.class);
            for (ThreadPoolTaskScheduler s : schedulers.values()) {
                try {
                    logger.info("Shutting down ThreadPoolTaskScheduler {}", s.getThreadNamePrefix());
                    s.shutdown();
                } catch (Exception e) {
                    logger.warn("Failed to shutdown taskScheduler cleanly", e);
                }
            }
        } catch (Throwable t) {
            logger.debug("No ThreadPoolTaskScheduler available or failed to shutdown", t);
        }
    }
}
