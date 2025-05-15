package com.project.fanla.dto;

import com.project.fanla.enums.MatchStatus;

public class MatchUpdateDto {
    private Long matchId;
    private Integer homeScore;
    private Integer awayScore;
    private MatchStatus status;
    private Long activeSoundId;
    private String activeSoundTitle;
    private String activeSoundUrl;
    private String soundStartTime;
    private String elapsedTimeOnPause;
    private Long currentMillisecond;
    private String activeSoundLyrics; // Lyrics JSON data
    
    public MatchUpdateDto() {
    }
    
    public MatchUpdateDto(Long matchId, Integer homeScore, Integer awayScore, MatchStatus status,
                         Long activeSoundId, String activeSoundTitle, String activeSoundUrl,
                         String soundStartTime, String elapsedTimeOnPause, Long currentMillisecond,
                         String activeSoundLyrics) {
        this.matchId = matchId;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.status = status;
        this.activeSoundId = activeSoundId;
        this.activeSoundTitle = activeSoundTitle;
        this.activeSoundUrl = activeSoundUrl;
        this.soundStartTime = soundStartTime;
        this.elapsedTimeOnPause = elapsedTimeOnPause;
        this.currentMillisecond = currentMillisecond;
        this.activeSoundLyrics = activeSoundLyrics;
    }

    // Getters and Setters
    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
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

    public Long getActiveSoundId() {
        return activeSoundId;
    }

    public void setActiveSoundId(Long activeSoundId) {
        this.activeSoundId = activeSoundId;
    }

    public String getActiveSoundTitle() {
        return activeSoundTitle;
    }

    public void setActiveSoundTitle(String activeSoundTitle) {
        this.activeSoundTitle = activeSoundTitle;
    }

    public String getActiveSoundUrl() {
        return activeSoundUrl;
    }

    public void setActiveSoundUrl(String activeSoundUrl) {
        this.activeSoundUrl = activeSoundUrl;
    }

    public String getSoundStartTime() {
        return soundStartTime;
    }

    public void setSoundStartTime(String soundStartTime) {
        this.soundStartTime = soundStartTime;
    }

    public String getElapsedTimeOnPause() {
        return elapsedTimeOnPause;
    }

    public void setElapsedTimeOnPause(String elapsedTimeOnPause) {
        this.elapsedTimeOnPause = elapsedTimeOnPause;
    }
    
    public Long getCurrentMillisecond() {
        return currentMillisecond;
    }
    
    public void setCurrentMillisecond(Long currentMillisecond) {
        this.currentMillisecond = currentMillisecond;
    }
    
    public String getActiveSoundLyrics() {
        return activeSoundLyrics;
    }
    
    public void setActiveSoundLyrics(String activeSoundLyrics) {
        this.activeSoundLyrics = activeSoundLyrics;
    }
}
