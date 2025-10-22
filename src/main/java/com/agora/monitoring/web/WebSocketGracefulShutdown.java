package com.agora.monitoring.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PreDestroy;

@Component
public class WebSocketGracefulShutdown implements SmartLifecycle {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketGracefulShutdown.class);

    private final ApplicationContext ctx;
    private volatile boolean running = true;
    private volatile boolean alreadyShutdown = false;

    @Autowired
    public WebSocketGracefulShutdown(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void start() { running = true; }

    @PreDestroy
    public void preDestroy() {
        logger.debug("WebSocketGracefulShutdown.preDestroy() invoked");
        performShutdown();
    }

    @Override
    public void stop() {
        logger.debug("WebSocketGracefulShutdown.stop() invoked");
        performShutdown();
    }

    private synchronized void performShutdown() {
        if (alreadyShutdown) {
            return;
        }
        alreadyShutdown = true;

        // Stop messaging handlers politely
        tryStopClass("org.springframework.web.socket.messaging.SubProtocolWebSocketHandler");
        tryStopClass("org.springframework.messaging.simp.broker.SimpleBrokerMessageHandler");

        // Aggressively shutdown executors to prevent thread leaks
        shutdownAllExecutors();

        running = false;
    }

    private void shutdownAllExecutors() {
        try {
            // 1. Force shutdown of the main task scheduler (used by @Scheduled tasks)
            if (ctx.containsBean("taskScheduler")) {
                Object scheduler = ctx.getBean("taskScheduler");
                if (scheduler instanceof org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler) {
                    org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler tpts =
                        (org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler) scheduler;
                    java.util.concurrent.ScheduledExecutorService executor = tpts.getScheduledExecutor();
                    if (executor != null && !executor.isShutdown()) {
                        logger.info("🔴 Force shutting down taskScheduler");
                        executor.shutdownNow();
                        try {
                            if (!executor.awaitTermination(2, java.util.concurrent.TimeUnit.SECONDS)) {
                                logger.warn("⚠️ TaskScheduler did not terminate in time");
                            } else {
                                logger.info("✅ TaskScheduler terminated");
                            }
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }

            // 2. Shutdown channel executors directly by bean name
            String[] executorNames = {"inboundChannelExecutor", "outboundChannelExecutor"};
            for (String execName : executorNames) {
                logger.info("🔍 Checking for executor bean: {}", execName);
                if (!ctx.containsBean(execName)) {
                    logger.warn("⚠️ Executor bean {} not found in context", execName);
                    continue;
                }
                Object executor = ctx.getBean(execName);
                if (executor == null) {
                    logger.warn("⚠️ Executor bean {} is null", execName);
                    continue;
                }
                logger.info("🔍 Found executor bean {}: {}", execName, executor.getClass().getName());

                java.util.concurrent.ExecutorService execService = null;

                // Handle Spring's ThreadPoolTaskExecutor
                if (executor instanceof org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor) {
                    org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor tpte =
                        (org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor) executor;
                    execService = tpte.getThreadPoolExecutor();
                    logger.info("✅ Got ThreadPoolExecutor from {}", execName);
                } else if (executor instanceof java.util.concurrent.ExecutorService) {
                    execService = (java.util.concurrent.ExecutorService) executor;
                    logger.info("✅ {} is directly an ExecutorService", execName);
                } else {
                    logger.warn("⚠️ Bean {} is not a recognized executor type: {}", execName, executor.getClass().getName());
                }

                if (execService != null && !execService.isShutdown()) {
                    logger.info("🔴 Shutting down {} executor", execName);
                    execService.shutdownNow();
                    try {
                        if (!execService.awaitTermination(2, java.util.concurrent.TimeUnit.SECONDS)) {
                            logger.warn("⚠️ {} did not terminate in time", execName);
                        } else {
                            logger.info("✅ {} terminated", execName);
                        }
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else if (execService == null) {
                    logger.warn("⚠️ Could not extract ExecutorService from {}", execName);
                } else {
                    logger.info("✅ {} already shutdown", execName);
                }
            }
        } catch (Exception ex) {
            logger.warn("Error during executor shutdown", ex);
        }
    }

    @Override
    public boolean isRunning() { return running; }

    @Override
    public int getPhase() {
        // During Spring shutdown, components with HIGHER phases stop FIRST
        // SimpleBrokerMessageHandler has phase 0
        // We need to stop BEFORE Tomcat checks for thread leaks
        // Use a high positive phase to ensure we run early in shutdown
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isAutoStartup() { return true; }

    @Override
    public void stop(Runnable callback) { try { stop(); } finally { callback.run(); } }

    private void tryStopClass(String className) {
        try {
            Class<?> cls = Class.forName(className);
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> beans = (java.util.Map) ctx.getBeansOfType((Class) cls);
            for (Object b : beans.values()) {
                try {
                    if (b instanceof org.springframework.context.Lifecycle) {
                        ((org.springframework.context.Lifecycle) b).stop();
                        logger.info("Stopped {} bean {}", className, b.getClass().getName());
                    } else {
                        try {
                            java.lang.reflect.Method m = b.getClass().getMethod("stop");
                            m.invoke(b);
                            logger.info("Invoked stop() on {} bean {}", className, b.getClass().getName());
                        } catch (NoSuchMethodException ns) {
                            // ignore
                        }
                    }
                } catch (Throwable ex) {
                    logger.debug("Failed to stop bean of type {}", className, ex);
                }
            }
        } catch (ClassNotFoundException cnf) {
            // not present - ignore
        }
    }
}
