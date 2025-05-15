package com.project.fanla.service;

import com.project.fanla.dto.AdminUserDto;
import com.project.fanla.entity.Team;
import com.project.fanla.entity.User;
import com.project.fanla.enums.Role;
import com.project.fanla.repository.TeamRepository;
import com.project.fanla.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminUserService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserService(UserRepository userRepository, TeamRepository teamRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<AdminUserDto> getAllAdmins() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == Role.ADMIN)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AdminUserDto getAdminById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));
        
        if (user.getRole() != Role.ADMIN) {
            throw new RuntimeException("User with id: " + id + " is not an admin");
        }
        
        return convertToDto(user);
    }

    @Transactional
    public AdminUserDto createAdmin(AdminUserDto adminUserDto) {
        // Validate admin doesn't already exist
        if (userRepository.existsByUsername(adminUserDto.getUsername())) {
            throw new RuntimeException("User with username '" + adminUserDto.getUsername() + "' already exists");
        }
        
        if (userRepository.existsByEmail(adminUserDto.getEmail())) {
            throw new RuntimeException("User with email '" + adminUserDto.getEmail() + "' already exists");
        }
        
        // Find team
        Team team = teamRepository.findById(adminUserDto.getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + adminUserDto.getTeamId()));
        
        // Convert DTO to entity
        User user = new User();
        user.setUsername(adminUserDto.getUsername());
        user.setPassword(passwordEncoder.encode(adminUserDto.getPassword()));
        user.setEmail(adminUserDto.getEmail());
        user.setRole(Role.ADMIN); // Ensure role is ADMIN
        user.setFirstName(adminUserDto.getFirstName());
        user.setLastName(adminUserDto.getLastName());
        user.setTeam(team);
        
        // Save entity
        User savedUser = userRepository.save(user);
        
        // Return DTO with generated ID
        return convertToDto(savedUser);
    }

    @Transactional
    public AdminUserDto updateAdmin(Long id, AdminUserDto adminUserDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));
        
        if (user.getRole() != Role.ADMIN) {
            throw new RuntimeException("User with id: " + id + " is not an admin");
        }
        
        // Check if username is being changed and if new username already exists
        if (!user.getUsername().equals(adminUserDto.getUsername()) && 
                userRepository.existsByUsername(adminUserDto.getUsername())) {
            throw new RuntimeException("User with username '" + adminUserDto.getUsername() + "' already exists");
        }
        
        // Check if email is being changed and if new email already exists
        if (!user.getEmail().equals(adminUserDto.getEmail()) && 
                userRepository.existsByEmail(adminUserDto.getEmail())) {
            throw new RuntimeException("User with email '" + adminUserDto.getEmail() + "' already exists");
        }
        
        // Find team if it's being updated
        Team team = user.getTeam();
        if (user.getTeam() == null || !user.getTeam().getId().equals(adminUserDto.getTeamId())) {
            team = teamRepository.findById(adminUserDto.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found with id: " + adminUserDto.getTeamId()));
        }
        
        // Update entity
        user.setUsername(adminUserDto.getUsername());
        // Only update password if it's provided
        if (adminUserDto.getPassword() != null && !adminUserDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(adminUserDto.getPassword()));
        }
        user.setEmail(adminUserDto.getEmail());
        user.setFirstName(adminUserDto.getFirstName());
        user.setLastName(adminUserDto.getLastName());
        user.setTeam(team);
        
        // Save updated entity
        User updatedUser = userRepository.save(user);
        
        // Return updated DTO
        return convertToDto(updatedUser);
    }

    @Transactional
    public void deleteAdmin(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));
        
        if (user.getRole() != Role.ADMIN) {
            throw new RuntimeException("User with id: " + id + " is not an admin");
        }
        
        userRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public List<AdminUserDto> getAdminsByTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + teamId));
        
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == Role.ADMIN && user.getTeam() != null && user.getTeam().getId().equals(teamId))
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    // Helper method to convert entity to DTO
    private AdminUserDto convertToDto(User user) {
        AdminUserDto dto = new AdminUserDto(
                user.getId(),
                user.getUsername(),
                null, // Don't include password in response
                user.getEmail(),
                user.getRole(),
                user.getFirstName(),
                user.getLastName(),
                user.getTeam() != null ? user.getTeam().getId() : null
        );
        
        if (user.getTeam() != null) {
            dto.setTeamName(user.getTeam().getName());
        }
        
        return dto;
    }
}
