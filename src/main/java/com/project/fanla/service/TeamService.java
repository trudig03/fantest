package com.project.fanla.service;

import com.project.fanla.dto.TeamDto;
import com.project.fanla.entity.Country;
import com.project.fanla.entity.Team;
import com.project.fanla.repository.CountryRepository;
import com.project.fanla.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final CountryRepository countryRepository;

    public TeamService(TeamRepository teamRepository, CountryRepository countryRepository) {
        this.teamRepository = teamRepository;
        this.countryRepository = countryRepository;
    }

    @Transactional(readOnly = true)
    public List<TeamDto> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TeamDto getTeamById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + id));
        return convertToDto(team);
    }

    @Transactional
    public TeamDto createTeam(TeamDto teamDto) {
        // Validate team doesn't already exist
        if (teamRepository.existsByName(teamDto.getName())) {
            throw new RuntimeException("Team with name '" + teamDto.getName() + "' already exists");
        }
        
        // Find country
        Country country = countryRepository.findById(teamDto.getCountryId())
                .orElseThrow(() -> new RuntimeException("Country not found with id: " + teamDto.getCountryId()));
        
        // Convert DTO to entity
        Team team = new Team();
        team.setName(teamDto.getName());
        team.setLogo(teamDto.getLogo());
        team.setDescription(teamDto.getDescription());
        team.setCountry(country);
        
        // Save entity
        Team savedTeam = teamRepository.save(team);
        
        // Return DTO with generated ID
        return convertToDto(savedTeam);
    }

    @Transactional
    public TeamDto updateTeam(Long id, TeamDto teamDto) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + id));
        
        // Check if name is being changed and if new name already exists
        if (!team.getName().equals(teamDto.getName()) && 
                teamRepository.existsByName(teamDto.getName())) {
            throw new RuntimeException("Team with name '" + teamDto.getName() + "' already exists");
        }
        
        // Find country if it's being updated
        Country country = team.getCountry();
        if (!team.getCountry().getId().equals(teamDto.getCountryId())) {
            country = countryRepository.findById(teamDto.getCountryId())
                    .orElseThrow(() -> new RuntimeException("Country not found with id: " + teamDto.getCountryId()));
        }
        
        // Update entity
        team.setName(teamDto.getName());
        team.setLogo(teamDto.getLogo());
        team.setDescription(teamDto.getDescription());
        team.setCountry(country);
        
        // Save updated entity
        Team updatedTeam = teamRepository.save(team);
        
        // Return updated DTO
        return convertToDto(updatedTeam);
    }

    @Transactional
    public void deleteTeam(Long id) {
        if (!teamRepository.existsById(id)) {
            throw new RuntimeException("Team not found with id: " + id);
        }
        
        teamRepository.deleteById(id);
    }
    
    @Transactional(readOnly = true)
    public List<TeamDto> getTeamsByCountry(Long countryId) {
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new RuntimeException("Country not found with id: " + countryId));
        
        return teamRepository.findByCountry(country).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    // Helper method to convert entity to DTO
    private TeamDto convertToDto(Team team) {
        TeamDto dto = new TeamDto(
                team.getId(),
                team.getName(),
                team.getLogo(),
                team.getDescription(),
                team.getCountry().getId()
        );
        dto.setCountryName(team.getCountry().getName());
        return dto;
    }
}
