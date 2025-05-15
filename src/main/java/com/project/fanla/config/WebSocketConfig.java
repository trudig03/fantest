package com.project.fanla.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Topic'ler için prefix tanımlama - tüm maç güncellemeleri buradan yayınlanacak
        config.enableSimpleBroker("/topic");
        // App'e gelen mesajlar için prefix
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // WebSocket bağlantı noktası - tüm origin'lere izin ver
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // Tüm kaynaklardan erişime izin ver
                .withSockJS()
                .setSessionCookieNeeded(false) // Session cookie'lerini devre dışı bırak
                .setWebSocketEnabled(true) // WebSocket'i etkinleştir
                .setHeartbeatTime(25000); // Heartbeat süresini ayarla
    }
    
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        // Mesaj boyutu sınırlarını artır
        registration.setMessageSizeLimit(8192) // default 64 * 1024
                   .setSendBufferSizeLimit(8192) // default 512 * 1024
                   .setSendTimeLimit(10000); // default 10 * 10000
    }
    
    /**
     * JSR-356 WebSocket API için ServerEndpointExporter bean'i
     * Bu bean, @ServerEndpoint anotasyonu ile işaretlenmiş sınıfları WebSocket endpoint'i olarak kaydeder
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
