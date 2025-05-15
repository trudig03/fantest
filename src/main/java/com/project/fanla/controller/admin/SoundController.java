package com.project.fanla.controller.admin;

import com.project.fanla.dto.SoundDto;
import com.project.fanla.entity.User;
import com.project.fanla.repository.UserRepository;
import com.project.fanla.service.SoundService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/sounds")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class SoundController {

    private final SoundService soundService;
    private final UserRepository userRepository;

    public SoundController(SoundService soundService, UserRepository userRepository) {
        this.soundService = soundService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<SoundDto>> getMyTeamSounds() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Extract username from authentication
        String username = authentication.getName();
        
        // Find user by username
        User adminUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        
        if (adminUser.getTeam() == null) {
            throw new RuntimeException("Admin is not associated with any team");
        }
        
        return ResponseEntity.ok(soundService.getSoundsByTeam(adminUser.getTeam().getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SoundDto> getSoundById(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Extract username from authentication
        String username = authentication.getName();
        
        // Find user by username
        User adminUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        
        if (adminUser.getTeam() == null) {
            throw new RuntimeException("Admin is not associated with any team");
        }
        
        SoundDto sound = soundService.getSoundById(id);
        
        // Verify the sound belongs to the admin's team
        if (!sound.getTeamId().equals(adminUser.getTeam().getId())) {
            throw new RuntimeException("You do not have permission to view this sound");
        }
        
        return ResponseEntity.ok(sound);
    }



    @PostMapping
    public ResponseEntity<SoundDto> createSound(@RequestBody SoundDto soundDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Extract username from authentication
        String username = authentication.getName();
        
        // Find user by username
        User adminUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        
        if (adminUser.getTeam() == null) {
            throw new RuntimeException("Admin is not associated with any team");
        }
        
        // Force the teamId to be the admin's team
        soundDto.setTeamId(adminUser.getTeam().getId());
        
        SoundDto createdSound = soundService.createSound(soundDto, adminUser.getId());
        return new ResponseEntity<>(createdSound, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SoundDto> updateSound(@PathVariable Long id, @RequestBody SoundDto soundDto) {
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
        SoundDto existingSound = soundService.getSoundById(id);
        if (!existingSound.getTeamId().equals(adminUser.getTeam().getId())) {
            throw new RuntimeException("You do not have permission to update this sound");
        }
        
        // Ensure teamId cannot be changed
        soundDto.setTeamId(adminUser.getTeam().getId());
        
        SoundDto updatedSound = soundService.updateSound(id, soundDto);
        return ResponseEntity.ok(updatedSound);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSound(@PathVariable Long id) {
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
        SoundDto sound = soundService.getSoundById(id);
        if (!sound.getTeamId().equals(adminUser.getTeam().getId())) {
            throw new RuntimeException("You do not have permission to delete this sound");
        }
        
        soundService.deleteSound(id);
        return ResponseEntity.noContent().build();
    }
}
