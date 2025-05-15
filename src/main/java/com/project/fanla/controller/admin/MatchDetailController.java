package com.project.fanla.controller.admin;

import com.project.fanla.dto.MatchDto;
import com.project.fanla.dto.SoundActionDto;
import com.project.fanla.dto.SoundDto;
import com.project.fanla.entity.User;
import com.project.fanla.enums.MatchStatus;
import com.project.fanla.repository.UserRepository;
import com.project.fanla.service.MatchService;
import com.project.fanla.service.MatchWebSocketService;
import com.project.fanla.service.SoundService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/match-detail")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class MatchDetailController {

    private final MatchService matchService;
    private final SoundService soundService;
    private final MatchWebSocketService matchWebSocketService;
    private final UserRepository userRepository;

    public MatchDetailController(MatchService matchService, 
                               SoundService soundService,
                               MatchWebSocketService matchWebSocketService,
                               UserRepository userRepository) {
        this.matchService = matchService;
        this.soundService = soundService;
        this.matchWebSocketService = matchWebSocketService;
        this.userRepository = userRepository;
    }

    /**
     * Giriş yapmış adminin takımının maç detaylarını getirir
     */
    @GetMapping("/{matchId}")
    public ResponseEntity<Map<String, Object>> getMatchDetail(@PathVariable Long matchId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Extract username from authentication
        String username = authentication.getName();
        
        // Find user by username
        User adminUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        
        if (adminUser.getTeam() == null) {
            throw new RuntimeException("Admin is not associated with any team");
        }
        
        // Maç bilgilerini getir
        MatchDto match = matchService.getMatchById(matchId);
        
        // Maçın admin'in takımına ait olup olmadığını kontrol et
        if (!match.getTeamId().equals(adminUser.getTeam().getId())) {
            throw new RuntimeException("You do not have permission to view this match");
        }
        
        // Takımın tüm seslerini getir
        List<SoundDto> teamSounds = soundService.getSoundsByTeam(adminUser.getTeam().getId());
        
        // Yanıt için tüm verileri bir araya getir
        Map<String, Object> response = new HashMap<>();
        response.put("match", match);
        response.put("sounds", teamSounds);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Admin kullanıcısının takımına ait tüm sesleri getirir
     */
    @GetMapping("/{matchId}/sounds")
    public ResponseEntity<List<SoundDto>> getTeamSounds(@PathVariable Long matchId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Extract username from authentication
        String username = authentication.getName();
        
        // Find user by username
        User adminUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        
        if (adminUser.getTeam() == null) {
            throw new RuntimeException("Admin is not associated with any team");
        }
        
        // Maç bilgilerini getir
        MatchDto match = matchService.getMatchById(matchId);
        
        // Maçın admin'in takımına ait olup olmadığını kontrol et
        if (!match.getTeamId().equals(adminUser.getTeam().getId())) {
            throw new RuntimeException("You do not have permission to view this match");
        }
        
        // Takımın tüm seslerini getir
        List<SoundDto> teamSounds = soundService.getSoundsByTeam(adminUser.getTeam().getId());
        
        return ResponseEntity.ok(teamSounds);
    }

    /**
     * Maç skorunu günceller
     */
    @PutMapping("/{matchId}/score")
    public ResponseEntity<MatchDto> updateMatchScore(
            @PathVariable Long matchId,
            @RequestParam Integer homeScore,
            @RequestParam Integer awayScore) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Extract username from authentication
        String username = authentication.getName();
        
        // Find user by username
        User adminUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        
        if (adminUser.getTeam() == null) {
            throw new RuntimeException("Admin is not associated with any team");
        }
        
        // Maç bilgilerini getir
        MatchDto match = matchService.getMatchById(matchId);
        
        // Maçın admin'in takımına ait olup olmadığını kontrol et
        if (!match.getTeamId().equals(adminUser.getTeam().getId())) {
            throw new RuntimeException("You do not have permission to update this match");
        }
        
        // WebSocket service ile skoru güncelle
        matchWebSocketService.updateMatchStatus(matchId, homeScore, awayScore, null);
        
        // Güncellenmiş maç bilgilerini getir
        return ResponseEntity.ok(matchService.getMatchById(matchId));
    }

    /**
     * Maç durumunu günceller (PLANNED, LIVE, COMPLETED, CANCELLED)
     */
    @PutMapping("/{matchId}/status")
    public ResponseEntity<MatchDto> updateMatchStatus(
            @PathVariable Long matchId,
            @RequestParam MatchStatus status) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Extract username from authentication
        String username = authentication.getName();
        
        // Find user by username
        User adminUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        
        if (adminUser.getTeam() == null) {
            throw new RuntimeException("Admin is not associated with any team");
        }
        
        // Maç bilgilerini getir
        MatchDto match = matchService.getMatchById(matchId);
        
        // Maçın admin'in takımına ait olup olmadığını kontrol et
        if (!match.getTeamId().equals(adminUser.getTeam().getId())) {
            throw new RuntimeException("You do not have permission to update this match");
        }
        
        // WebSocket service ile durumu güncelle
        matchWebSocketService.updateMatchStatus(matchId, null, null, status);
        
        // Güncellenmiş maç bilgilerini getir
        return ResponseEntity.ok(matchService.getMatchById(matchId));
    }

    /**
     * Maçta ses çalma, duraklatma veya durdurma işlemlerini gerçekleştirir
     */
    @PostMapping("/{matchId}/sound")
    public ResponseEntity<MatchDto> controlSound(
            @PathVariable Long matchId,
            @RequestBody SoundActionDto soundActionDto) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Extract username from authentication
        String username = authentication.getName();
        
        // Find user by username
        User adminUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        
        if (adminUser.getTeam() == null) {
            throw new RuntimeException("Admin is not associated with any team");
        }
        
        // Maç bilgilerini getir
        MatchDto match = matchService.getMatchById(matchId);
        
        // Maçın admin'in takımına ait olup olmadığını kontrol et
        if (!match.getTeamId().equals(adminUser.getTeam().getId())) {
            throw new RuntimeException("You do not have permission to control sounds for this match");
        }
        
        // Ses ID'si belirtilmişse, sesin takıma ait olup olmadığını kontrol et
        if (soundActionDto.getSoundId() != null) {
            SoundDto sound = soundService.getSoundById(soundActionDto.getSoundId());
            if (!sound.getTeamId().equals(adminUser.getTeam().getId())) {
                throw new RuntimeException("You do not have permission to use this sound");
            }
        }
        
        // Match ID'yi DTO'ya ekle
        soundActionDto.setMatchId(matchId);
        
        // WebSocket service ile ses kontrolü yap
        matchWebSocketService.updateMatchSound(soundActionDto);
        
        // Güncellenmiş maç bilgilerini getir
        return ResponseEntity.ok(matchService.getMatchById(matchId));
    }
}
