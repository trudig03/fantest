package com.project.fanla.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.project.fanla.enums.MatchStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Example: "Fenerbahçe vs Galatasaray"

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "opponent_team_id")
    private Team opponentTeam;

    private String location; // Match venue

    private LocalDateTime matchDate;
    
    private Integer homeScore = 0;
    
    private Integer awayScore = 0;
    
    @Enumerated(EnumType.STRING)
    private MatchStatus status = MatchStatus.PLANNED;

    @OneToOne
    @JoinColumn(name = "active_sound_id")
    private Sound activeSound;

    // Son çalınan ses bilgileri (ses durdurulduğunda da gösterilecek)
    private Long lastSoundId;
    private String lastSoundTitle;
    private String lastSoundUrl;
    private String lastSoundLyrics;

    private LocalDateTime soundStartTime; // UTC time when the sound started
    
    private Long elapsedTimeOnPause; // Milliseconds elapsed when the sound was paused
    
    private Long currentMillisecond; // Current position in the sound in milliseconds
    
    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy; // Admin who created this match
    
    // Constructors
    public Match() {
    }
    
    public Match(Long id, String name, Team team, Team opponentTeam, String location, 
                 LocalDateTime matchDate, User createdBy) {
        this.id = id;
        this.name = name;
        this.team = team;
        this.opponentTeam = opponentTeam;
        this.location = location;
        this.matchDate = matchDate;
        this.homeScore = 0;
        this.awayScore = 0;
        this.status = MatchStatus.PLANNED;
        this.createdBy = createdBy;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Team getTeam() {
        return team;
    }
    
    public void setTeam(Team team) {
        this.team = team;
    }
    
    public Team getOpponentTeam() {
        return opponentTeam;
    }
    
    public void setOpponentTeam(Team opponentTeam) {
        this.opponentTeam = opponentTeam;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public LocalDateTime getMatchDate() {
        return matchDate;
    }
    
    public void setMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }
    
    public Integer getHomeScore() {
        return homeScore;
    }
    
    public void setHomeScore(Integer homeScore) {
        this.homeScore = homeScore;
    }
    
    public Integer getAwayScore() {
        return awayScore;
    }
    
    public void setAwayScore(Integer awayScore) {
        this.awayScore = awayScore;
    }
    
    public MatchStatus getStatus() {
        return status;
    }
    
    public void setStatus(MatchStatus status) {
        this.status = status;
    }
    
    public Sound getActiveSound() {
        return activeSound;
    }
    
    public void setActiveSound(Sound activeSound) {
        this.activeSound = activeSound;
        
        // Aktif ses ayarlanırken son çalınan ses bilgilerini de güncelle
        if (activeSound != null) {
            this.lastSoundId = activeSound.getId();
            this.lastSoundTitle = activeSound.getTitle();
            this.lastSoundUrl = activeSound.getSoundUrl();
            if (activeSound.getLyrics() != null) {
                this.lastSoundLyrics = activeSound.getLyrics().getLyricsText();
            }
        }
    }
    
    public Long getLastSoundId() {
        return lastSoundId;
    }
    
    public void setLastSoundId(Long lastSoundId) {
        this.lastSoundId = lastSoundId;
    }
    
    public String getLastSoundTitle() {
        return lastSoundTitle;
    }
    
    public void setLastSoundTitle(String lastSoundTitle) {
        this.lastSoundTitle = lastSoundTitle;
    }
    
    public String getLastSoundUrl() {
        return lastSoundUrl;
    }
    
    public void setLastSoundUrl(String lastSoundUrl) {
        this.lastSoundUrl = lastSoundUrl;
    }
    
    public String getLastSoundLyrics() {
        return lastSoundLyrics;
    }
    
    public void setLastSoundLyrics(String lastSoundLyrics) {
        this.lastSoundLyrics = lastSoundLyrics;
    }
    
    public LocalDateTime getSoundStartTime() {
        return soundStartTime;
    }
    
    public void setSoundStartTime(LocalDateTime soundStartTime) {
        this.soundStartTime = soundStartTime;
    }
    
    public Long getElapsedTimeOnPause() {
        return elapsedTimeOnPause;
    }
    
    public void setElapsedTimeOnPause(Long elapsedTimeOnPause) {
        this.elapsedTimeOnPause = elapsedTimeOnPause;
    }
    
    public Long getCurrentMillisecond() {
        return currentMillisecond;
    }
    
    public void setCurrentMillisecond(Long currentMillisecond) {
        this.currentMillisecond = currentMillisecond;
    }
    
    public User getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}


