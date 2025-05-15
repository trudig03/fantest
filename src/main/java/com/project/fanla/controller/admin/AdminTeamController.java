package com.project.fanla.controller.admin;

import com.project.fanla.dto.TeamUpdateDto;
import com.project.fanla.entity.User;
import com.project.fanla.repository.UserRepository;
import com.project.fanla.service.AdminTeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/team")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminTeamController {

    private final AdminTeamService adminTeamService;
    private final UserRepository userRepository;

    public AdminTeamController(AdminTeamService adminTeamService, UserRepository userRepository) {
        this.adminTeamService = adminTeamService;
        this.userRepository = userRepository;
    }

    @GetMapping("/my-team")
    public ResponseEntity<TeamUpdateDto> getMyTeam() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Extract username from authentication
        String username = authentication.getName();
        
        // Find user by username
        User adminUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        
        TeamUpdateDto teamDto = adminTeamService.getTeamByAdmin(adminUser.getId());
        return ResponseEntity.ok(teamDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamUpdateDto> getTeamById(@PathVariable Long id) {
        return ResponseEntity.ok(adminTeamService.getTeamById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamUpdateDto> updateTeam(
            @PathVariable Long id, 
            @RequestBody TeamUpdateDto teamUpdateDto) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Extract username from authentication
        String username = authentication.getName();
        
        // Find user by username
        User adminUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
        
        TeamUpdateDto updatedTeam = adminTeamService.updateTeam(id, teamUpdateDto, adminUser.getId());
        return ResponseEntity.ok(updatedTeam);
    }
}
