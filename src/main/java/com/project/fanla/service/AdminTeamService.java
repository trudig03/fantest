package com.project.fanla.service;

import com.project.fanla.dto.TeamUpdateDto;
import com.project.fanla.entity.Country;
import com.project.fanla.entity.Team;
import com.project.fanla.entity.User;
import com.project.fanla.repository.CountryRepository;
import com.project.fanla.repository.TeamRepository;
import com.project.fanla.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminTeamService {

    private final TeamRepository teamRepository;
    private final CountryRepository countryRepository;
    private final UserRepository userRepository;

    public AdminTeamService(TeamRepository teamRepository, CountryRepository countryRepository,
                           UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.countryRepository = countryRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public TeamUpdateDto getTeamById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + id));
        return convertToDto(team);
    }
    
    @Transactional(readOnly = true)
    public TeamUpdateDto getTeamByAdmin(Long adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + adminId));
        
        if (admin.getTeam() == null) {
            throw new RuntimeException("Admin is not associated with any team");
        }
        
        return convertToDto(admin.getTeam());
    }

    @Transactional
    public TeamUpdateDto updateTeam(Long teamId, TeamUpdateDto teamUpdateDto, Long adminId) {
        // Verify that the admin belongs to the team they're trying to update
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + adminId));
        
        if (admin.getTeam() == null || !admin.getTeam().getId().equals(teamId)) {
            throw new RuntimeException("Admin does not have permission to update this team");
        }
        
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + teamId));
        
        // Only update fields that are provided
        if (teamUpdateDto.getName() != null) {
            // Check if name is being changed and if new name already exists
            if (!team.getName().equals(teamUpdateDto.getName()) && 
                    teamRepository.existsByName(teamUpdateDto.getName())) {
                throw new RuntimeException("Team with name '" + teamUpdateDto.getName() + "' already exists");
            }
            team.setName(teamUpdateDto.getName());
        }
        
        if (teamUpdateDto.getLogo() != null) {
            team.setLogo(teamUpdateDto.getLogo());
        }
        
        if (teamUpdateDto.getDescription() != null) {
            team.setDescription(teamUpdateDto.getDescription());
        }
        
        // Country can only be updated if provided
        if (teamUpdateDto.getCountryId() != null && 
                (team.getCountry() == null || !team.getCountry().getId().equals(teamUpdateDto.getCountryId()))) {
            Country country = countryRepository.findById(teamUpdateDto.getCountryId())
                    .orElseThrow(() -> new RuntimeException("Country not found with id: " + teamUpdateDto.getCountryId()));
            team.setCountry(country);
        }
        
        Team updatedTeam = teamRepository.save(team);
        return convertToDto(updatedTeam);
    }
    
    // Helper method to convert entity to DTO
    private TeamUpdateDto convertToDto(Team team) {
        TeamUpdateDto dto = new TeamUpdateDto();
        dto.setId(team.getId());
        dto.setName(team.getName());
        dto.setLogo(team.getLogo());
        dto.setDescription(team.getDescription());
        
        if (team.getCountry() != null) {
            dto.setCountryId(team.getCountry().getId());
            dto.setCountryName(team.getCountry().getName());
        }
        
        return dto;
    }
}
