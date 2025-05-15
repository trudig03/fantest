package com.project.fanla.controller.admin;

import com.project.fanla.dto.LyricsDto;
import com.project.fanla.dto.SoundDto;
import com.project.fanla.entity.User;
import com.project.fanla.repository.UserRepository;
import com.project.fanla.service.LyricsService;
import com.project.fanla.service.SoundService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/lyrics")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class LyricsController {

    private final LyricsService lyricsService;
    private final UserRepository userRepository;
    private final SoundService soundService;

    public LyricsController(LyricsService lyricsService, UserRepository userRepository, SoundService soundService) {
        this.lyricsService = lyricsService;
        this.userRepository = userRepository;
        this.soundService = soundService;
    }

    @GetMapping
    public ResponseEntity<List<LyricsDto>> getMyTeamLyrics() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Extract username from authentication
        String username = authentication.getName();
        
        // Find user by username
        User adminUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        
        if (adminUser.getTeam() == null) {
            throw new RuntimeException("Admin is not associated with any team");
        }
        
        // Get all sounds for the team
        List<SoundDto> teamSounds = soundService.getSoundsByTeam(adminUser.getTeam().getId());
        
        // Get lyrics for each sound
        List<LyricsDto> teamLyrics = new ArrayList<>();
        for (SoundDto sound : teamSounds) {
            LyricsDto lyrics = lyricsService.getLyricsBySoundId(sound.getId());
            if (lyrics != null) {
                teamLyrics.add(lyrics);
            }
        }
        
        return ResponseEntity.ok(teamLyrics);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LyricsDto> getLyricsById(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Extract username from authentication
        String username = authentication.getName();
        
        // Find user by username
        User adminUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        
        if (adminUser.getTeam() == null) {
            throw new RuntimeException("Admin is not associated with any team");
        }
        
        LyricsDto lyrics = lyricsService.getLyricsById(id);
        
        // Get the sound to verify team ownership
        SoundDto sound = soundService.getSoundById(lyrics.getSoundId());
        
        // Verify the sound belongs to the admin's team
        if (!sound.getTeamId().equals(adminUser.getTeam().getId())) {
            throw new RuntimeException("You do not have permission to view these lyrics");
        }
        
        return ResponseEntity.ok(lyrics);
    }

    @GetMapping("/sound/{soundId}")
    public ResponseEntity<LyricsDto> getLyricsBySoundId(@PathVariable Long soundId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Extract username from authentication
        String username = authentication.getName();
        
        // Find user by username
        User adminUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        
        if (adminUser.getTeam() == null) {
            throw new RuntimeException("Admin is not associated with any team");
        }
        
        // Get the sound to verify team ownership
        SoundDto sound = soundService.getSoundById(soundId);
        
        // Verify the sound belongs to the admin's team
        if (!sound.getTeamId().equals(adminUser.getTeam().getId())) {
            throw new RuntimeException("You do not have permission to view these lyrics");
        }
        
        LyricsDto lyricsDto = lyricsService.getLyricsBySoundId(soundId);
        if (lyricsDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lyricsDto);
    }

    @PostMapping
    public ResponseEntity<LyricsDto> createLyrics(@RequestBody LyricsDto lyricsDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Extract username from authentication
        String username = authentication.getName();
        
        // Find user by username
        User adminUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        
        if (adminUser.getTeam() == null) {
            throw new RuntimeException("Admin is not associated with any team");
        }
        
        // Verify the sound belongs to the admin's team
        SoundDto sound = soundService.getSoundById(lyricsDto.getSoundId());
        if (!sound.getTeamId().equals(adminUser.getTeam().getId())) {
            throw new RuntimeException("You do not have permission to create lyrics for this sound");
        }
        
        LyricsDto createdLyrics = lyricsService.createLyrics(lyricsDto);
        return new ResponseEntity<>(createdLyrics, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LyricsDto> updateLyrics(@PathVariable Long id, @RequestBody LyricsDto lyricsDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Extract username from authentication
        String username = authentication.getName();
        
        // Find user by username
        User adminUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        
        if (adminUser.getTeam() == null) {
            throw new RuntimeException("Admin is not associated with any team");
        }
        
        // Get existing lyrics
        LyricsDto existingLyrics = lyricsService.getLyricsById(id);
        
        // Verify the sound belongs to the admin's team
        SoundDto sound = soundService.getSoundById(existingLyrics.getSoundId());
        if (!sound.getTeamId().equals(adminUser.getTeam().getId())) {
            throw new RuntimeException("You do not have permission to update these lyrics");
        }
        
        // Don't allow changing the soundId to a sound from another team
        if (lyricsDto.getSoundId() != null && !lyricsDto.getSoundId().equals(existingLyrics.getSoundId())) {
            SoundDto newSound = soundService.getSoundById(lyricsDto.getSoundId());
            if (!newSound.getTeamId().equals(adminUser.getTeam().getId())) {
                throw new RuntimeException("You do not have permission to associate lyrics with this sound");
            }
        }
        
        // Handle lyricsData field if present
        if (lyricsDto.getLyricsData() != null) {
            try {
                // Convert lyricsData to a proper JSON string
                ObjectMapper mapper = new ObjectMapper();
                String lyricsJson = mapper.writeValueAsString(lyricsDto.getLyricsData());
                lyricsDto.setLyricsText(lyricsJson);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error processing lyrics data: " + e.getMessage());
            }
        } else if (lyricsDto.getLyricsText() == null) {
            // If neither lyricsData nor lyricsText is provided, keep the existing value
            lyricsDto.setLyricsText(existingLyrics.getLyricsText());
        }
        
        LyricsDto updatedLyrics = lyricsService.updateLyrics(id, lyricsDto);
        return ResponseEntity.ok(updatedLyrics);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLyrics(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Extract username from authentication
        String username = authentication.getName();
        
        // Find user by username
        User adminUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        
        if (adminUser.getTeam() == null) {
            throw new RuntimeException("Admin is not associated with any team");
        }
        
        // Get lyrics to verify ownership
        LyricsDto lyrics = lyricsService.getLyricsById(id);
        
        // Verify the sound belongs to the admin's team
        SoundDto sound = soundService.getSoundById(lyrics.getSoundId());
        if (!sound.getTeamId().equals(adminUser.getTeam().getId())) {
            throw new RuntimeException("You do not have permission to delete these lyrics");
        }
        
        lyricsService.deleteLyrics(id);
        return ResponseEntity.noContent().build();
    }
}
