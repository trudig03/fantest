package com.project.fanla.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "lyrics")
public class Lyrics {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "sound_id", nullable = false)
    private Sound sound;
    
    @Column(name = "lyrics_text", columnDefinition = "TEXT")
    private String lyricsText = ""; // Default to empty string instead of null
    
    // Constructors
    public Lyrics() {
    }
    
    public Lyrics(Long id, Sound sound, String lyricsText) {
        this.id = id;
        this.sound = sound;
        this.lyricsText = lyricsText;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Sound getSound() {
        return sound;
    }
    
    public void setSound(Sound sound) {
        this.sound = sound;
    }
    
    public String getLyricsText() {
        return lyricsText;
    }
    
    public void setLyricsText(String lyricsText) {
        // Prevent null values, use empty string instead
        this.lyricsText = (lyricsText != null) ? lyricsText : "";
    }
}
