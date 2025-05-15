package com.project.fanla.dto;

import com.project.fanla.enums.SoundStatus;

public class SoundActionDto {
    private Long matchId;
    private Long soundId;
    private SoundStatus action; // PLAYING, PAUSED, STOPPED
    private String startTime; // ISO format timestamp
    private String elapsedTimeOnPause; // For resuming from pause
    private Long currentMillisecond; // Current position in milliseconds
    private String lyricsText; // Lyrics JSON data
    
    public SoundActionDto() {
    }
    
    public SoundActionDto(Long matchId, Long soundId, SoundStatus action, String startTime, 
                        String elapsedTimeOnPause, Long currentMillisecond, String lyricsText) {
        this.matchId = matchId;
        this.soundId = soundId;
        this.action = action;
        this.startTime = startTime;
        this.elapsedTimeOnPause = elapsedTimeOnPause;
        this.currentMillisecond = currentMillisecond;
        this.lyricsText = lyricsText;
    }

    // Getters and Setters
    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Long getSoundId() {
        return soundId;
    }

    public void setSoundId(Long soundId) {
        this.soundId = soundId;
    }

    public SoundStatus getAction() {
        return action;
    }

    public void setAction(SoundStatus action) {
        this.action = action;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
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
    
    public String getLyricsText() {
        return lyricsText;
    }
    
    public void setLyricsText(String lyricsText) {
        this.lyricsText = lyricsText;
    }
}
