package com.project.fanla.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    private String logo;
    
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;
    
    @OneToMany(mappedBy = "team")
    private List<User> admins;
    
    @OneToMany(mappedBy = "team")
    private List<Sound> sounds;
    
    @OneToMany(mappedBy = "team")
    private List<Match> homeMatches;
    
    @OneToMany(mappedBy = "opponentTeam")
    private List<Match> awayMatches;
    
    // Constructors
    public Team() {
    }
    
    public Team(Long id, String name, String logo, String description, Country country) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.description = description;
        this.country = country;
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
    
    public String getLogo() {
        return logo;
    }
    
    public void setLogo(String logo) {
        this.logo = logo;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Country getCountry() {
        return country;
    }
    
    public void setCountry(Country country) {
        this.country = country;
    }
    
    public List<User> getAdmins() {
        return admins;
    }
    
    public void setAdmins(List<User> admins) {
        this.admins = admins;
    }
    
    public List<Sound> getSounds() {
        return sounds;
    }
    
    public void setSounds(List<Sound> sounds) {
        this.sounds = sounds;
    }
    
    public List<Match> getHomeMatches() {
        return homeMatches;
    }
    
    public void setHomeMatches(List<Match> homeMatches) {
        this.homeMatches = homeMatches;
    }
    
    public List<Match> getAwayMatches() {
        return awayMatches;
    }
    
    public void setAwayMatches(List<Match> awayMatches) {
        this.awayMatches = awayMatches;
    }
}
