package com.project.fanla.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.fanla.enums.SoundStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SoundDto {
    
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private Long creatorId;
    private String creatorUsername;
    private String soundUrl;
    private Long teamId;
    private String teamName;
    private Integer playlistOrder;
    private SoundStatus status;
    
    // Constructors
    public SoundDto() {
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public Long getCreatorId() {
        return creatorId;
    }
    
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }
    
    public String getCreatorUsername() {
        return creatorUsername;
    }
    
    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }
    
    public String getSoundUrl() {
        return soundUrl;
    }
    
    public void setSoundUrl(String soundUrl) {
        this.soundUrl = soundUrl;
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
    
    public Integer getPlaylistOrder() {
        return playlistOrder;
    }
    
    public void setPlaylistOrder(Integer playlistOrder) {
        this.playlistOrder = playlistOrder;
    }
    
    public SoundStatus getStatus() {
        return status;
    }
    
    public void setStatus(SoundStatus status) {
        this.status = status;
    }
}
