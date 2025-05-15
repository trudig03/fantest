package com.project.fanla.controller;

import com.project.fanla.dto.MatchUpdateDto;
import com.project.fanla.dto.SoundActionDto;
import com.project.fanla.entity.User;
import com.project.fanla.repository.UserRepository;
import com.project.fanla.service.MatchWebSocketService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class WebSocketController {

    private final MatchWebSocketService matchWebSocketService;
    private final UserRepository userRepository;

    public WebSocketController(MatchWebSocketService matchWebSocketService, UserRepository userRepository) {
        this.matchWebSocketService = matchWebSocketService;
        this.userRepository = userRepository;
    }

    /**
     * Admin tarafından gelen ses kontrolü mesajlarını işler
     */
    @MessageMapping("/match.sound")
    public void handleSoundAction(@Payload SoundActionDto soundActionDto, Principal principal) {
        // Kullanıcı kontrolü
        String username = principal.getName();
        User adminUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        
        if (adminUser.getTeam() == null) {
            throw new RuntimeException("Admin is not associated with any team");
        }
        
        // Ses kontrolü işlemini gerçekleştir
        matchWebSocketService.updateMatchSound(soundActionDto);
    }

    /**
     * Admin tarafından gelen maç durum güncellemelerini işler
     */
    @MessageMapping("/match.update")
    public void handleMatchUpdate(@Payload MatchUpdateDto updateDto, Principal principal) {
        // Kullanıcı kontrolü
        String username = principal.getName();
        User adminUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        
        if (adminUser.getTeam() == null) {
            throw new RuntimeException("Admin is not associated with any team");
        }
        
        // Maç bilgilerini güncelle
        matchWebSocketService.updateMatchStatus(
            updateDto.getMatchId(), 
            updateDto.getHomeScore(), 
            updateDto.getAwayScore(), 
            updateDto.getStatus()
        );
    }
}