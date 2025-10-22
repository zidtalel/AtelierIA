package com.agora.monitoring.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class WebSocketEventListener {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    // Utiliser un Set pour tracker précisément les sessions actives
    private final Set<String> activeSessions = ConcurrentHashMap.newKeySet();
    private final AtomicInteger totalConnections = new AtomicInteger(0);

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        // Ajouter la session au Set (évite les doublons)
        boolean wasAdded = activeSessions.add(sessionId);
        int total = totalConnections.incrementAndGet();

        if (wasAdded) {
            logger.warn("🟢 WebSocket CONNECT - Session: {} | Active: {} | Total: {}",
                sessionId, activeSessions.size(), total);
        } else {
            logger.warn("⚠️ WebSocket CONNECT (duplicate) - Session: {} | Active: {} | Total: {}",
                sessionId, activeSessions.size(), total);
        }
    }

    @EventListener
    public void handleWebSocketConnectedListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        logger.info("✅ WebSocket CONNECTED - Session: {}", sessionId);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();

        // Retirer la session du Set (évite les compteurs négatifs)
        boolean wasRemoved = activeSessions.remove(sessionId);

        if (wasRemoved) {
            logger.warn("🔴 WebSocket DISCONNECT - Session: {} | Active remaining: {}",
                sessionId, activeSessions.size());
        } else {
            logger.warn("⚠️ WebSocket DISCONNECT (unknown session) - Session: {} | Active remaining: {}",
                sessionId, activeSessions.size());
        }
    }

    @EventListener
    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        String destination = headerAccessor.getDestination();
        logger.debug("📥 WebSocket SUBSCRIBE - Session: {} | Destination: {}",
            sessionId, destination);
    }

    @EventListener
    public void handleWebSocketUnsubscribeListener(SessionUnsubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        logger.debug("📤 WebSocket UNSUBSCRIBE - Session: {}", sessionId);
    }

    public int getActiveConnections() {
        return activeSessions.size();
    }

    public int getTotalConnections() {
        return totalConnections.get();
    }
}
