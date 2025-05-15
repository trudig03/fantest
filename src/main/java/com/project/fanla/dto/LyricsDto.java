package com.project.fanla.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LyricsDto {
    
    private Long id;
    private Long soundId;
    private String soundTitle;
    private String lyricsText;
    private List<Map<String, Object>> lyricsData; // To handle [{lyric: "asdasd", second: 0}]
    
    // Constructors
    public LyricsDto() {
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getSoundId() {
        return soundId;
    }
    
    public void setSoundId(Long soundId) {
        this.soundId = soundId;
    }
    
    public String getSoundTitle() {
        return soundTitle;
    }
    
    public void setSoundTitle(String soundTitle) {
        this.soundTitle = soundTitle;
    }
    
    public String getLyricsText() {
        return lyricsText;
    }
    
    public void setLyricsText(String lyricsText) {
        this.lyricsText = lyricsText;
    }
    
    public List<Map<String, Object>> getLyricsData() {
        return lyricsData;
    }
    
    public void setLyricsData(List<Map<String, Object>> lyricsData) {
        this.lyricsData = lyricsData;
        // When lyricsData is set, also update lyricsText with a JSON representation
        if (lyricsData != null) {
            // Convert lyricsData to JSON string and store in lyricsText
            this.lyricsText = lyricsData.toString();
        }
    }
}
