package com.project.fanla.controller.admin;

import com.project.fanla.dto.MatchDto;
import com.project.fanla.entity.User;
import com.project.fanla.repository.UserRepository;
import com.project.fanla.service.MatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/matches")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class MatchController {

    private final MatchService matchService;
    private final UserRepository userRepository;

    public MatchController(MatchService matchService, UserRepository userRepository) {
        this.matchService = matchService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<MatchDto>> getMyTeamMatches() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Extract username from authentication
        String username = authentication.getName();
        
        // Find user by username
        User adminUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        
        if (adminUser.getTeam() == null) {
            throw new RuntimeException("Admin is not associated with any team");
        }
        
        return ResponseEntity.ok(matchService.getMatchesByTeam(adminUser.getTeam().getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MatchDto> getMatchById(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Extract username from authentication
        String username = authentication.getName();
        
        // Find user by username
        User adminUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        
        if (adminUser.getTeam() == null) {
            throw new RuntimeException("Admin is not associated with any team");
        }
        
        MatchDto match = matchService.getMatchById(id);
        
        // Verify the match belongs to the admin's team
        if (!match.getTeamId().equals(adminUser.getTeam().getId())) {
            throw new RuntimeException("You do not have permission to view this match");
        }
        
        return ResponseEntity.ok(match);
    }



    @PostMapping
    public ResponseEntity<MatchDto> createMatch(@RequestBody MatchDto matchDto) {
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
        matchDto.setTeamId(adminUser.getTeam().getId());
        
        MatchDto createdMatch = matchService.createMatch(matchDto, adminUser.getId());
        return new ResponseEntity<>(createdMatch, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MatchDto> updateMatch(@PathVariable Long id, @RequestBody MatchDto matchDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Extract username from authentication
        String username = authentication.getName();
        
        // Find user by username
        User adminUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        
        if (adminUser.getTeam() == null) {
            throw new RuntimeException("Admin is not associated with any team");
        }
        
        // Verify the match belongs to the admin's team
        MatchDto existingMatch = matchService.getMatchById(id);
        if (!existingMatch.getTeamId().equals(adminUser.getTeam().getId())) {
            throw new RuntimeException("You do not have permission to update this match");
        }
        
        // Ensure teamId cannot be changed
        matchDto.setTeamId(adminUser.getTeam().getId());
        
        MatchDto updatedMatch = matchService.updateMatch(id, matchDto);
        return ResponseEntity.ok(updatedMatch);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Extract username from authentication
        String username = authentication.getName();
        
        // Find user by username
        User adminUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        
        if (adminUser.getTeam() == null) {
            throw new RuntimeException("Admin is not associated with any team");
        }
        
        // Verify the match belongs to the admin's team
        MatchDto match = matchService.getMatchById(id);
        if (!match.getTeamId().equals(adminUser.getTeam().getId())) {
            throw new RuntimeException("You do not have permission to delete this match");
        }
        
        matchService.deleteMatch(id);
        return ResponseEntity.noContent().build();
    }
}
