package com.project.fanla.entity;

import jakarta.persistence.*;

import com.project.fanla.enums.SoundStatus;

import java.util.List;

@Entity
@Table(name = "sounds")
public class Sound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    
    private String description;
    
    private String imageUrl; // URL for the sound image/cover
    
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator; // Who added this sound

    private String soundUrl; // URL of the sound file (e.g., S3 or CDN)

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
    
    @OneToOne(mappedBy = "sound")
    private Lyrics lyrics; // Şarkı sözleri
    
    private Integer playlistOrder; // Order in the match playlist
    
    @Enumerated(EnumType.STRING)
    private SoundStatus status = SoundStatus.STOPPED; // şarkı durumu
    
    // Constructors
    public Sound() {
    }
    
    public Sound(Long id, String title, String description, String imageUrl, User creator, 
                 String soundUrl, Team team, Integer playlistOrder) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.creator = creator;
        this.soundUrl = soundUrl;
        this.team = team;
        this.playlistOrder = playlistOrder;
        this.status = SoundStatus.STOPPED;
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
    
    public User getCreator() {
        return creator;
    }
    
    public void setCreator(User creator) {
        this.creator = creator;
    }
    
    public String getSoundUrl() {
        return soundUrl;
    }
    
    public void setSoundUrl(String soundUrl) {
        this.soundUrl = soundUrl;
    }
    
    public Team getTeam() {
        return team;
    }
    
    public void setTeam(Team team) {
        this.team = team;
    }
    
    public Lyrics getLyrics() {
        return lyrics;
    }
    
    public void setLyrics(Lyrics lyrics) {
        this.lyrics = lyrics;
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
