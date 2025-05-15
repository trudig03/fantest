package com.project.fanla.service;

import com.project.fanla.dto.SoundDto;
import com.project.fanla.entity.Sound;
import com.project.fanla.entity.Team;
import com.project.fanla.entity.User;
import com.project.fanla.enums.SoundStatus;
import com.project.fanla.repository.SoundRepository;
import com.project.fanla.repository.TeamRepository;
import com.project.fanla.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SoundService {

    private final SoundRepository soundRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public SoundService(SoundRepository soundRepository, TeamRepository teamRepository, 
                       UserRepository userRepository) {
        this.soundRepository = soundRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<SoundDto> getAllSounds() {
        return soundRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<SoundDto> getSoundsByTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + teamId));
        
        return soundRepository.findByTeam(team).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SoundDto getSoundById(Long id) {
        Sound sound = soundRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sound not found with id: " + id));
        return convertToDto(sound);
    }

    @Transactional
    public SoundDto createSound(SoundDto soundDto, Long creatorId) {
        Team team = teamRepository.findById(soundDto.getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + soundDto.getTeamId()));
        
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + creatorId));
        
        Sound sound = new Sound();
        sound.setTitle(soundDto.getTitle());
        sound.setDescription(soundDto.getDescription());
        sound.setImageUrl(soundDto.getImageUrl());
        sound.setCreator(creator);
        sound.setSoundUrl(soundDto.getSoundUrl());
        sound.setTeam(team);
        
        // Set default values for optional fields
        sound.setPlaylistOrder(soundDto.getPlaylistOrder());
        sound.setStatus(soundDto.getStatus() != null ? soundDto.getStatus() : SoundStatus.STOPPED);
        
        Sound savedSound = soundRepository.save(sound);
        return convertToDto(savedSound);
    }

    @Transactional
    public SoundDto updateSound(Long id, SoundDto soundDto) {
        Sound sound = soundRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sound not found with id: " + id));
        
        // Only update fields that are provided
        if (soundDto.getTitle() != null) {
            sound.setTitle(soundDto.getTitle());
        }
        
        if (soundDto.getDescription() != null) {
            sound.setDescription(soundDto.getDescription());
        }
        
        if (soundDto.getImageUrl() != null) {
            sound.setImageUrl(soundDto.getImageUrl());
        }
        
        if (soundDto.getSoundUrl() != null) {
            sound.setSoundUrl(soundDto.getSoundUrl());
        }
        
        if (soundDto.getTeamId() != null) {
            Team team = teamRepository.findById(soundDto.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found with id: " + soundDto.getTeamId()));
            sound.setTeam(team);
        }
        
        if (soundDto.getPlaylistOrder() != null) {
            sound.setPlaylistOrder(soundDto.getPlaylistOrder());
        }
        
        if (soundDto.getStatus() != null) {
            sound.setStatus(soundDto.getStatus());
        }
        
        Sound updatedSound = soundRepository.save(sound);
        return convertToDto(updatedSound);
    }

    @Transactional
    public void deleteSound(Long id) {
        if (!soundRepository.existsById(id)) {
            throw new RuntimeException("Sound not found with id: " + id);
        }
        
        soundRepository.deleteById(id);
    }
    
    // Helper method to convert entity to DTO
    private SoundDto convertToDto(Sound sound) {
        SoundDto dto = new SoundDto();
        dto.setId(sound.getId());
        dto.setTitle(sound.getTitle());
        dto.setDescription(sound.getDescription());
        dto.setImageUrl(sound.getImageUrl());
        
        if (sound.getCreator() != null) {
            dto.setCreatorId(sound.getCreator().getId());
            dto.setCreatorUsername(sound.getCreator().getUsername());
        }
        
        dto.setSoundUrl(sound.getSoundUrl());
        
        if (sound.getTeam() != null) {
            dto.setTeamId(sound.getTeam().getId());
            dto.setTeamName(sound.getTeam().getName());
        }
        
        dto.setPlaylistOrder(sound.getPlaylistOrder());
        dto.setStatus(sound.getStatus());
        
        return dto;
    }
}
