package com.project.fanla.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.fanla.dto.MatchDto;
import com.project.fanla.service.MatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket endpoint for match updates
 * This endpoint uses JSR-356 WebSocket API for direct WebSocket connections
 * without STOMP protocol
 */
@Component
@ServerEndpoint(value = "/match-socket/{matchId}")
public class MatchWebSocketEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(MatchWebSocketEndpoint.class);
    
    // Static reference to match service (injected by Spring)
    private static MatchService matchService;
    
    // Object mapper for JSON serialization
    private static ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
            .configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    
    // Store all active sessions by matchId
    private static final Map<Long, Map<String, Session>> matchSessions = new ConcurrentHashMap<>();
    
    @Autowired
    public void setMatchService(MatchService matchService) {
        MatchWebSocketEndpoint.matchService = matchService;
    }
    
    /**
     * Called when a new WebSocket connection is established
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("matchId") Long matchId) {
        logger.info("New WebSocket connection opened for match: {}, sessionId: {}", matchId, session.getId());
        
        // Add session to the match sessions map
        matchSessions.computeIfAbsent(matchId, k -> new ConcurrentHashMap<>())
                     .put(session.getId(), session);
        
        // Send initial match data
        try {
            sendMatchData(session, matchId);
        } catch (Exception e) {
            logger.error("Error sending initial match data", e);
        }
    }
    
    /**
     * Called when a WebSocket connection is closed
     */
    @OnClose
    public void onClose(Session session, @PathParam("matchId") Long matchId) {
        logger.info("WebSocket connection closed for match: {}, sessionId: {}", matchId, session.getId());
        
        // Remove session from the match sessions map
        if (matchSessions.containsKey(matchId)) {
            matchSessions.get(matchId).remove(session.getId());
            
            // If no more sessions for this match, remove the match entry
            if (matchSessions.get(matchId).isEmpty()) {
                matchSessions.remove(matchId);
            }
        }
    }
    
    /**
     * Called when an error occurs in the WebSocket connection
     */
    @OnError
    public void onError(Session session, @PathParam("matchId") Long matchId, Throwable throwable) {
        logger.error("Error in WebSocket connection for match: {}, sessionId: {}", matchId, session.getId(), throwable);
        
        // Close the session on error
        try {
            session.close();
        } catch (IOException e) {
            logger.error("Error closing WebSocket session", e);
        }
    }
    
    /**
     * Called when a message is received from the client
     * This is not used in this implementation as we only send updates to clients
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("matchId") Long matchId) {
        logger.info("Received message from client for match: {}, message: {}", matchId, message);
        // We don't process incoming messages in this implementation
    }
    
    /**
     * Send match data to a specific session
     */
    private void sendMatchData(Session session, Long matchId) {
        try {
            if (session.isOpen()) {
                // Önce maç entity'sini al
                com.project.fanla.entity.Match match = matchService.getMatchEntity(matchId);
                
                // Güncellenmiş maç bilgilerini al
                MatchDto matchDto = matchService.getMatchById(matchId);
                
                // Eğer şarkı çalıyorsa, currentMillisecond değerini doğru şekilde ayarla
                if (match.getActiveSound() != null && 
                    com.project.fanla.enums.SoundStatus.PLAYING.equals(match.getActiveSound().getStatus()) &&
                    match.getSoundStartTime() != null) {
                    
                    // Ses başlangıç zamanından şu ana kadar geçen süreyi hesapla (milisaniye cinsinden)
                    java.time.LocalDateTime now = java.time.LocalDateTime.now();
                    long elapsedMillis = java.time.Duration.between(match.getSoundStartTime(), now).toMillis();
                    
                    // Duraklatma anında geçen süre varsa, onu ekle
                    if (match.getElapsedTimeOnPause() != null) {
                        elapsedMillis += match.getElapsedTimeOnPause();
                    }
                    
                    // DTO'daki currentMillisecond değerini güncelle
                    matchDto.setCurrentMillisecond(elapsedMillis);
                    logger.info("Updated currentMillisecond for session {}: {}", session.getId(), elapsedMillis);
                }
                
                // JSON formatında gönder
                String json = objectMapper.writeValueAsString(matchDto);
                session.getBasicRemote().sendText(json);
                logger.info("Sent match data to session {}, currentMillisecond: {}", 
                           session.getId(), matchDto.getCurrentMillisecond());
            }
        } catch (Exception e) {
            logger.error("Error sending match data to session: {}", session.getId(), e);
        }
    }
    
    /**
     * Broadcast match update to all connected clients for a specific match
     * This method is called from MatchWebSocketService when a match is updated
     */
    public static void broadcastMatchUpdate(Long matchId) {
        if (matchService == null) {
            logger.error("MatchService is not initialized");
            return;
        }
        
        try {
            // Önce maç entity'sini al
            com.project.fanla.entity.Match match = matchService.getMatchEntity(matchId);
            
            // Güncellenmiş maç bilgilerini al
            MatchDto matchDto = matchService.getMatchById(matchId);
            
            // Eğer şarkı çalıyorsa, currentMillisecond değerini doğru şekilde ayarla
            if (match.getActiveSound() != null && 
                com.project.fanla.enums.SoundStatus.PLAYING.equals(match.getActiveSound().getStatus()) && 
                match.getSoundStartTime() != null) {
                
                // Ses başlangıç zamanından şu ana kadar geçen süreyi hesapla (milisaniye cinsinden)
                java.time.LocalDateTime now = java.time.LocalDateTime.now();
                long elapsedMillis = java.time.Duration.between(match.getSoundStartTime(), now).toMillis();
                
                // Duraklatma anında geçen süre varsa, onu ekle
                if (match.getElapsedTimeOnPause() != null) {
                    elapsedMillis += match.getElapsedTimeOnPause();
                }
                
                // DTO'daki currentMillisecond değerini güncelle
                matchDto.setCurrentMillisecond(elapsedMillis);
                logger.info("Updated currentMillisecond for broadcast: {}", elapsedMillis);
            }
            
            logger.info("Retrieved match data for broadcast: {}", matchDto);
            
            try {
                // JSON formatında gönder
                String json = objectMapper.writeValueAsString(matchDto);
                logger.info("Serialized match data to JSON: {}", json);
                
                Map<String, Session> sessions = matchSessions.get(matchId);
                if (sessions != null) {
                    sessions.values().forEach(session -> {
                        try {
                            if (session.isOpen()) {
                                session.getBasicRemote().sendText(json);
                                logger.info("Sent update to session: {}, currentMillisecond: {}", 
                                           session.getId(), matchDto.getCurrentMillisecond());
                            }
                        } catch (IOException e) {
                            logger.error("Error sending match update to session: {}", session.getId(), e);
                        }
                    });
                    logger.info("Broadcast match update to {} clients for match: {}", sessions.size(), matchId);
                } else {
                    logger.warn("No active sessions found for match: {}", matchId);
                }
            } catch (Exception e) {
                logger.error("Error serializing match data to JSON for match: {}", matchId, e);
                throw e; // Rethrow to be caught by outer catch block
            }
        } catch (Exception e) {
            logger.error("Error broadcasting match update for match: {}", matchId, e);
        }
    }
    
    // updateCurrentMillisecondStatic metodu kaldırıldı. 
    // Artık currentMillisecond hesaplaması doğrudan sendMatchData ve broadcastMatchUpdate metodlarında yapılıyor.
}
