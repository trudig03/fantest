package com.project.fanla.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Bu sınıf WebSocket bağlantıları için güvenlik kontrollerini devre dışı bırakır.
 * Tüm WebSocket endpoint'lerine erişim izni verir.
 */
@Configuration
@EnableWebSecurity
@Order(1) // Diğer güvenlik yapılandırmalarından önce çalışması için
public class WebSecurityDisabledConfig {

    @Bean
    public SecurityFilterChain webSocketSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/ws/**", "/match-socket/**", "/topic/**", "/app/**", "/websocket-test.html", "/direct-websocket-test.html")
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );
        
        return http.build();
    }
}
