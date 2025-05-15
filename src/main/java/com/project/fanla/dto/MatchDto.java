package com.project.fanla.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.fanla.enums.MatchStatus;
import com.project.fanla.enums.SoundStatus;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MatchDto {
    
    private Long id;
    private String name;
    private Long teamId;
    private String teamName;
    private Long opponentTeamId;
    private String opponentTeamName;
    private String location;
    private LocalDateTime matchDate;
    private Integer homeScore;
    private Integer awayScore;
    private MatchStatus status;
    private Long activeSoundId;
    private String activeSoundTitle;
    private String activeSoundUrl;
    private SoundStatus soundStatus; // Ses durumu (PLAYING, PAUSED, STOPPED)
    private LocalDateTime soundStartTime;
    private Long elapsedTimeOnPause;
    private Long currentMillisecond;
    private String activeSoundLyrics; // Lyrics JSON data
    
    // Constructors
    public MatchDto() {
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
    
    public Long getTeamId() {
        return teamId;
    }
    
    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }
    
    public String getTeamName() {
        return teamName;
    }
    
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    
    public Long getOpponentTeamId() {
        return opponentTeamId;
    }
    
    public void setOpponentTeamId(Long opponentTeamId) {
        this.opponentTeamId = opponentTeamId;
    }
    
    public String getOpponentTeamName() {
        return opponentTeamName;
    }
    
    public void setOpponentTeamName(String opponentTeamName) {
        this.opponentTeamName = opponentTeamName;
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
    
    public SoundStatus getSoundStatus() {
        return soundStatus;
    }
    
    public void setSoundStatus(SoundStatus soundStatus) {
        this.soundStatus = soundStatus;
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
    
    public String getActiveSoundLyrics() {
        return activeSoundLyrics;
    }
    
    public void setActiveSoundLyrics(String activeSoundLyrics) {
        this.activeSoundLyrics = activeSoundLyrics;
    }
    
    @Override
    public String toString() {
        return "MatchDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", teamId=" + teamId +
                ", teamName='" + teamName + '\'' +
                ", opponentTeamId=" + opponentTeamId +
                ", opponentTeamName='" + opponentTeamName + '\'' +
                ", location='" + location + '\'' +
                ", matchDate=" + matchDate +
                ", homeScore=" + homeScore +
                ", awayScore=" + awayScore +
                ", status=" + status +
                ", activeSoundId=" + activeSoundId +
                ", activeSoundTitle='" + activeSoundTitle + '\'' +
                ", activeSoundUrl='" + activeSoundUrl + '\'' +
                ", soundStatus=" + soundStatus +
                ", soundStartTime=" + soundStartTime +
                ", elapsedTimeOnPause=" + elapsedTimeOnPause +
                ", currentMillisecond=" + currentMillisecond +
                ", activeSoundLyrics='" + activeSoundLyrics + '\'' +
                '}';
    }
}
