package com.project.fanla.service;

import com.project.fanla.dto.LyricsDto;
import com.project.fanla.entity.Lyrics;
import com.project.fanla.entity.Sound;
import com.project.fanla.repository.LyricsRepository;
import com.project.fanla.repository.SoundRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LyricsService {

    private final LyricsRepository lyricsRepository;
    private final SoundRepository soundRepository;

    public LyricsService(LyricsRepository lyricsRepository, SoundRepository soundRepository) {
        this.lyricsRepository = lyricsRepository;
        this.soundRepository = soundRepository;
    }

    @Transactional(readOnly = true)
    public List<LyricsDto> getAllLyrics() {
        return lyricsRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LyricsDto getLyricsById(Long id) {
        Lyrics lyrics = lyricsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lyrics not found with id: " + id));
        return convertToDto(lyrics);
    }
    
    @Transactional(readOnly = true)
    public LyricsDto getLyricsBySoundId(Long soundId) {
        Sound sound = soundRepository.findById(soundId)
                .orElseThrow(() -> new RuntimeException("Sound not found with id: " + soundId));
        
        Optional<Lyrics> lyricsOpt = lyricsRepository.findBySound(sound);
        if (lyricsOpt.isPresent()) {
            return convertToDto(lyricsOpt.get());
        } else {
            return null;
        }
    }

    @Transactional
    public LyricsDto createLyrics(LyricsDto lyricsDto) {
        Sound sound = soundRepository.findById(lyricsDto.getSoundId())
                .orElseThrow(() -> new RuntimeException("Sound not found with id: " + lyricsDto.getSoundId()));
        
        // Check if lyrics already exist for this sound
        Optional<Lyrics> existingLyrics = lyricsRepository.findBySound(sound);
        if (existingLyrics.isPresent()) {
            throw new RuntimeException("Lyrics already exist for sound with id: " + lyricsDto.getSoundId());
        }
        
        Lyrics lyrics = new Lyrics();
        lyrics.setSound(sound);
        lyrics.setLyricsText(lyricsDto.getLyricsText());
        
        Lyrics savedLyrics = lyricsRepository.save(lyrics);
        return convertToDto(savedLyrics);
    }

    @Transactional
    public LyricsDto updateLyrics(Long id, LyricsDto lyricsDto) {
        Lyrics lyrics = lyricsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lyrics not found with id: " + id));
        
        // Always update lyricsText, even if it's null in the DTO
        // The entity's setLyricsText method will handle null values
        lyrics.setLyricsText(lyricsDto.getLyricsText());
        
        // Only update sound if provided and different
        if (lyricsDto.getSoundId() != null && !lyrics.getSound().getId().equals(lyricsDto.getSoundId())) {
            Sound sound = soundRepository.findById(lyricsDto.getSoundId())
                    .orElseThrow(() -> new RuntimeException("Sound not found with id: " + lyricsDto.getSoundId()));
            
            // Check if lyrics already exist for this sound
            Optional<Lyrics> existingLyrics = lyricsRepository.findBySound(sound);
            if (existingLyrics.isPresent() && !existingLyrics.get().getId().equals(id)) {
                throw new RuntimeException("Lyrics already exist for sound with id: " + lyricsDto.getSoundId());
            }
            
            lyrics.setSound(sound);
        }
        
        Lyrics updatedLyrics = lyricsRepository.save(lyrics);
        return convertToDto(updatedLyrics);
    }

    @Transactional
    public void deleteLyrics(Long id) {
        if (!lyricsRepository.existsById(id)) {
            throw new RuntimeException("Lyrics not found with id: " + id);
        }
        
        lyricsRepository.deleteById(id);
    }
    
    // Helper method to convert entity to DTO
    private LyricsDto convertToDto(Lyrics lyrics) {
        LyricsDto dto = new LyricsDto();
        dto.setId(lyrics.getId());
        
        if (lyrics.getSound() != null) {
            dto.setSoundId(lyrics.getSound().getId());
            dto.setSoundTitle(lyrics.getSound().getTitle());
        }
        
        dto.setLyricsText(lyrics.getLyricsText());
        
        return dto;
    }
}
