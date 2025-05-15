package com.project.fanla.controller.fan;

import com.project.fanla.dto.MatchDto;
import com.project.fanla.dto.SoundDto;
import com.project.fanla.entity.User;
import com.project.fanla.repository.UserRepository;
import com.project.fanla.service.MatchService;
import com.project.fanla.service.SoundService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/fan/match-stream")
@PreAuthorize("permitAll()")
public class MatchStreamController {

    private final MatchService matchService;
    private final SoundService soundService;
    private final UserRepository userRepository;

    public MatchStreamController(MatchService matchService, 
                              SoundService soundService,
                              UserRepository userRepository) {
        this.matchService = matchService;
        this.soundService = soundService;
        this.userRepository = userRepository;
    }

    /**
     * Fan için maç detaylarını ve aktif ses bilgilerini getirir
     */
    @GetMapping("/{matchId}")
    public ResponseEntity<Map<String, Object>> getMatchStream(@PathVariable Long matchId) {
        // Maç bilgilerini getir
        MatchDto match = matchService.getMatchById(matchId);
        
        // Aktif ses bilgilerini getir
        SoundDto activeSound = null;
        if (match.getActiveSoundId() != null) {
            activeSound = soundService.getSoundById(match.getActiveSoundId());
        }
        
        // Yanıt için tüm verileri bir araya getir
        Map<String, Object> response = new HashMap<>();
        response.put("match", match);
        response.put("activeSound", activeSound);
        
        // WebSocket bağlantı bilgilerini ekle
        Map<String, String> websocketInfo = new HashMap<>();
        websocketInfo.put("endpoint", "/ws");
        websocketInfo.put("topic", "/topic/match/" + matchId);
        response.put("websocket", websocketInfo);
        
        return ResponseEntity.ok(response);
    }
}
