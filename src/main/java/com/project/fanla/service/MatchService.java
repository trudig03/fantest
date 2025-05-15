package com.project.fanla.service;



import com.project.fanla.dto.MatchDto;
import com.project.fanla.entity.Match;
import com.project.fanla.entity.Sound;
import com.project.fanla.entity.Team;
import com.project.fanla.entity.User;
import com.project.fanla.enums.MatchStatus;
import com.project.fanla.repository.MatchRepository;
import com.project.fanla.repository.SoundRepository;
import com.project.fanla.repository.TeamRepository;
import com.project.fanla.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final SoundRepository soundRepository;
    private final UserRepository userRepository;

    public MatchService(MatchRepository matchRepository, TeamRepository teamRepository, 
                       SoundRepository soundRepository, UserRepository userRepository) {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.soundRepository = soundRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<MatchDto> getAllMatches() {
        return matchRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<MatchDto> getMatchesByTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + teamId));
        
        return matchRepository.findByTeam(team).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MatchDto getMatchById(Long id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + id));
        return convertToDto(match);
    }
    
    /**
     * Maç entity'sini doğrudan almak için kullanılır
     * Bu metod, WebSocket endpoint'i tarafından kullanılır
     */
    @Transactional(readOnly = true)
    public Match getMatchEntity(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + id));
    }

    @Transactional
    public MatchDto createMatch(MatchDto matchDto, Long creatorId) {
        Team team = teamRepository.findById(matchDto.getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + matchDto.getTeamId()));
        
        Team opponentTeam = teamRepository.findById(matchDto.getOpponentTeamId())
                .orElseThrow(() -> new RuntimeException("Opponent team not found with id: " + matchDto.getOpponentTeamId()));
        
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + creatorId));
        
        Match match = new Match();
        match.setName(matchDto.getName());
        match.setTeam(team);
        match.setOpponentTeam(opponentTeam);
        match.setLocation(matchDto.getLocation());
        match.setMatchDate(matchDto.getMatchDate());
        match.setCreatedBy(creator);
        
        // Set default values for optional fields
        match.setHomeScore(matchDto.getHomeScore() != null ? matchDto.getHomeScore() : 0);
        match.setAwayScore(matchDto.getAwayScore() != null ? matchDto.getAwayScore() : 0);
        match.setStatus(matchDto.getStatus() != null ? matchDto.getStatus() : MatchStatus.PLANNED);
        
        // Only set active sound if provided
        if (matchDto.getActiveSoundId() != null) {
            Sound sound = soundRepository.findById(matchDto.getActiveSoundId())
                    .orElseThrow(() -> new RuntimeException("Sound not found with id: " + matchDto.getActiveSoundId()));
            match.setActiveSound(sound);
        }
        
        // Only set sound timing fields if provided
        if (matchDto.getSoundStartTime() != null) {
            match.setSoundStartTime(matchDto.getSoundStartTime());
        }
        
        if (matchDto.getElapsedTimeOnPause() != null) {
            match.setElapsedTimeOnPause(matchDto.getElapsedTimeOnPause());
        }
        
        if (matchDto.getCurrentMillisecond() != null) {
            match.setCurrentMillisecond(matchDto.getCurrentMillisecond());
        }
        
        Match savedMatch = matchRepository.save(match);
        return convertToDto(savedMatch);
    }

    @Transactional
    public MatchDto updateMatch(Long id, MatchDto matchDto) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + id));
        
        // Only update fields that are provided
        if (matchDto.getName() != null) {
            match.setName(matchDto.getName());
        }
        
        if (matchDto.getTeamId() != null) {
            Team team = teamRepository.findById(matchDto.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found with id: " + matchDto.getTeamId()));
            match.setTeam(team);
        }
        
        if (matchDto.getOpponentTeamId() != null) {
            Team opponentTeam = teamRepository.findById(matchDto.getOpponentTeamId())
                    .orElseThrow(() -> new RuntimeException("Opponent team not found with id: " + matchDto.getOpponentTeamId()));
            match.setOpponentTeam(opponentTeam);
        }
        
        if (matchDto.getLocation() != null) {
            match.setLocation(matchDto.getLocation());
        }
        
        if (matchDto.getMatchDate() != null) {
            match.setMatchDate(matchDto.getMatchDate());
        }
        
        if (matchDto.getHomeScore() != null) {
            match.setHomeScore(matchDto.getHomeScore());
        }
        
        if (matchDto.getAwayScore() != null) {
            match.setAwayScore(matchDto.getAwayScore());
        }
        
        if (matchDto.getStatus() != null) {
            match.setStatus(matchDto.getStatus());
        }
        
        // Update active sound if provided
        if (matchDto.getActiveSoundId() != null) {
            Sound sound = soundRepository.findById(matchDto.getActiveSoundId())
                    .orElseThrow(() -> new RuntimeException("Sound not found with id: " + matchDto.getActiveSoundId()));
            match.setActiveSound(sound);
        }
        
        // Update sound timing fields if provided
        if (matchDto.getSoundStartTime() != null) {
            match.setSoundStartTime(matchDto.getSoundStartTime());
        }
        
        if (matchDto.getElapsedTimeOnPause() != null) {
            match.setElapsedTimeOnPause(matchDto.getElapsedTimeOnPause());
        }
        
        if (matchDto.getCurrentMillisecond() != null) {
            match.setCurrentMillisecond(matchDto.getCurrentMillisecond());
        }
        
        Match updatedMatch = matchRepository.save(match);
        return convertToDto(updatedMatch);
    }

    @Transactional
    public void deleteMatch(Long id) {
        if (!matchRepository.existsById(id)) {
            throw new RuntimeException("Match not found with id: " + id);
        }
        
        matchRepository.deleteById(id);
    }
    
    // Helper method to convert entity to DTO
    private MatchDto convertToDto(Match match) {
        MatchDto dto = new MatchDto();
        dto.setId(match.getId());
        dto.setName(match.getName());
        
        if (match.getTeam() != null) {
            dto.setTeamId(match.getTeam().getId());
            dto.setTeamName(match.getTeam().getName());
        }
        
        if (match.getOpponentTeam() != null) {
            dto.setOpponentTeamId(match.getOpponentTeam().getId());
            dto.setOpponentTeamName(match.getOpponentTeam().getName());
        }
        
        dto.setLocation(match.getLocation());
        dto.setMatchDate(match.getMatchDate());
        dto.setHomeScore(match.getHomeScore());
        dto.setAwayScore(match.getAwayScore());
        dto.setStatus(match.getStatus());
        
        // Aktif ses varsa bilgilerini ekle
        if (match.getActiveSound() != null) {
            dto.setActiveSoundId(match.getActiveSound().getId());
            dto.setActiveSoundTitle(match.getActiveSound().getTitle());
            dto.setActiveSoundUrl(match.getActiveSound().getSoundUrl());
            dto.setSoundStatus(match.getActiveSound().getStatus()); // Ses durumunu ekle
            
            // Lyrics bilgilerini ekle
            if (match.getActiveSound().getLyrics() != null) {
                dto.setActiveSoundLyrics(match.getActiveSound().getLyrics().getLyricsText());
            }
        } else {
            // Aktif ses yoksa (durdurulmuşsa) STOPPED durumunu ekle
            dto.setSoundStatus(com.project.fanla.enums.SoundStatus.STOPPED);
            
            // Son çalınan ses bilgilerini ekle
            if (match.getLastSoundId() != null) {
                dto.setActiveSoundId(match.getLastSoundId());
                dto.setActiveSoundTitle(match.getLastSoundTitle());
                dto.setActiveSoundUrl(match.getLastSoundUrl());
                dto.setActiveSoundLyrics(match.getLastSoundLyrics());
            }
        }
        
        dto.setSoundStartTime(match.getSoundStartTime());
        dto.setElapsedTimeOnPause(match.getElapsedTimeOnPause());
        dto.setCurrentMillisecond(match.getCurrentMillisecond());
        
        return dto;
    }
}