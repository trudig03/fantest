package com.project.fanla.service;

import com.project.fanla.dto.MatchDto;
import com.project.fanla.dto.SoundActionDto;
import com.project.fanla.entity.Match;
import com.project.fanla.entity.Sound;
import com.project.fanla.enums.MatchStatus;
import com.project.fanla.enums.SoundStatus;
import com.project.fanla.repository.MatchRepository;
import com.project.fanla.repository.SoundRepository;
import com.project.fanla.repository.LyricsRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class MatchWebSocketService {

    private static final Logger logger = LoggerFactory.getLogger(MatchWebSocketService.class);
    private final SimpMessagingTemplate messagingTemplate;
    private final MatchRepository matchRepository;
    private final SoundRepository soundRepository;
    private final LyricsRepository lyricsRepository;
    private final MatchService matchService;
    
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    public MatchWebSocketService(SimpMessagingTemplate messagingTemplate, 
                                MatchRepository matchRepository,
                                SoundRepository soundRepository,
                                LyricsRepository lyricsRepository,
                                MatchService matchService) {
        this.messagingTemplate = messagingTemplate;
        this.matchRepository = matchRepository;
        this.soundRepository = soundRepository;
        this.lyricsRepository = lyricsRepository;
        this.matchService = matchService;
    }
    
    // Zamanlayıcıyı kaldırdık. Artık sadece ilk bağlantıda ve ses işlemlerinde (play, pause, stop) mesaj gönderilecek.
    
    /**
     * Maç bilgilerini günceller ve WebSocket üzerinden yayınlar
     * NOT: Bu metot sadece skor ve durum güncellemelerinde kullanılır, WebSocket güncellemesi göndermez
     */
    @Transactional
    public void updateMatchStatus(Long matchId, Integer homeScore, Integer awayScore, MatchStatus status) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + matchId));
        
        // Skor ve durum güncelleme
        if (homeScore != null) {
            match.setHomeScore(homeScore);
        }
        
        if (awayScore != null) {
            match.setAwayScore(awayScore);
        }
        
        if (status != null) {
            match.setStatus(status);
        }
        
        // Veritabanına kaydet
        matchRepository.save(match);
        
        // WebSocket güncellemesi gönderilmiyor - sadece ses işlemlerinde güncelleme gönderilecek
        logger.info("Match status updated for matchId: {}, but no WebSocket update sent", matchId);
    }
    
    /**
     * Maçta çalan sesi günceller ve WebSocket üzerinden yayınlar
     * NOT: Bu metot ses işlemlerinde (play, pause, stop) kullanılır ve WebSocket güncellemesi gönderir
     */
    @Transactional
    public void updateMatchSound(SoundActionDto soundActionDto) {
        logger.info("Updating match sound: {}", soundActionDto);
        
        Match match = matchRepository.findById(soundActionDto.getMatchId())
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + soundActionDto.getMatchId()));
        
        Sound sound = null;
        if (soundActionDto.getSoundId() != null) {
            sound = soundRepository.findById(soundActionDto.getSoundId())
                    .orElseThrow(() -> new RuntimeException("Sound not found with id: " + soundActionDto.getSoundId()));
            
            // Ses durumunu güncelle
            sound.setStatus(soundActionDto.getAction());
            soundRepository.save(sound);
        }
        
        // Maç bilgilerini güncelle
        if (SoundStatus.PLAYING.equals(soundActionDto.getAction())) {
            // Şarkı çalma işlemi
            match.setActiveSound(sound);
            
            // Ses başlangıç zamanını ayarla
            match.setSoundStartTime(LocalDateTime.now());
            
            // Milisaniye bilgisini ayarla (yeni şarkı başlatıldığında sıfırlanır)
            match.setCurrentMillisecond(0L);
            
            // Duraklatma zamanını sıfırla
            match.setElapsedTimeOnPause(null);
            
            logger.info("Sound PLAYING: matchId={}, soundId={}", match.getId(), sound.getId());
        } else if (SoundStatus.PAUSED.equals(soundActionDto.getAction())) {
            // Şarkı duraklatma işlemi
            if (soundActionDto.getCurrentMillisecond() != null) {
                // Client'tan gelen milisaniye değerini kullan
                match.setCurrentMillisecond(soundActionDto.getCurrentMillisecond());
                match.setElapsedTimeOnPause(soundActionDto.getCurrentMillisecond());
            }
            
            logger.info("Sound PAUSED: matchId={}, currentMs={}", match.getId(), match.getCurrentMillisecond());
        } else if (SoundStatus.STOPPED.equals(soundActionDto.getAction())) {
            // Şarkı durdurma işlemi
            match.setActiveSound(null);
            match.setSoundStartTime(null);
            match.setElapsedTimeOnPause(null);
            match.setCurrentMillisecond(0L); // Sıfır olarak ayarla, null değil
            
            logger.info("Sound STOPPED: matchId={}", match.getId());
        }
        
        // Veritabanına kaydet
        match = matchRepository.save(match);
        
        // WebSocket'e güncelleme gönder
        sendMatchUpdate(match);
    }
    
    /**
     * Maç bilgilerini WebSocket üzerinden yayınlar
     */
    private void sendMatchUpdate(Match match) {
        try {
            // Şarkı çalıyorsa, geçen süreyi hesapla ve currentMillisecond değerini güncelle
            updateCurrentMillisecond(match);
            
            // Maç DTO'sunu oluştur (tüm maç bilgilerini içerir)
            MatchDto matchDto = matchService.getMatchById(match.getId());
            
            // Log olarak gönderilen veriyi yazdır
            logger.info("Sending match update via WebSocket: {}", matchDto);
            
            // WebSocket'e gönder
            messagingTemplate.convertAndSend("/topic/match/" + match.getId(), matchDto);
            
            // Ayrıca doğrudan WebSocket endpoint'ine de gönder
            com.project.fanla.websocket.MatchWebSocketEndpoint.broadcastMatchUpdate(match.getId());
            
            logger.info("WebSocket update sent for matchId: {}", match.getId());
        } catch (Exception e) {
            logger.error("Error sending WebSocket update for matchId: {}", match.getId(), e);
        }
    }
    
    /**
     * Şarkı çalıyorsa, geçen süreyi hesaplar ve currentMillisecond değerini günceller
     */
    private void updateCurrentMillisecond(Match match) {
        // Eğer aktif bir ses varsa ve PLAYING durumundaysa
        if (match.getActiveSound() != null && 
            SoundStatus.PLAYING.equals(match.getActiveSound().getStatus()) && 
            match.getSoundStartTime() != null) {
            
            // Ses başlangıç zamanından şu ana kadar geçen süreyi hesapla (milisaniye cinsinden)
            LocalDateTime now = LocalDateTime.now();
            long elapsedMillis = java.time.Duration.between(match.getSoundStartTime(), now).toMillis();
            
            // Duraklatma anında geçen süre varsa, onu ekle
            if (match.getElapsedTimeOnPause() != null) {
                elapsedMillis += match.getElapsedTimeOnPause();
            }
            
            // CurrentMillisecond değerini güncelle
            match.setCurrentMillisecond(elapsedMillis);
            
            logger.info("Updated currentMillisecond for matchId: {}, value: {}", match.getId(), elapsedMillis);
            
            // Veritabanına kaydet
            matchRepository.save(match);
        } else if (match.getActiveSound() != null && 
                  SoundStatus.PAUSED.equals(match.getActiveSound().getStatus())) {
            // Duraklatma durumunda, mevcut currentMillisecond değerini koru
            logger.info("Sound is PAUSED, keeping currentMillisecond: {}", match.getCurrentMillisecond());
        }
    }
}
