package com.project.fanla.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CountryDto {
    
    private Long id;
    
    @NotBlank(message = "Country name is required")
    @Size(min = 2, max = 100, message = "Country name must be between 2 and 100 characters")
    private String name;
    
    @NotBlank(message = "Country short code is required")
    @Size(min = 2, max = 3, message = "Short code must be between 2 and 3 characters")
    private String shortCode;
    
    private String logoUrl;
    
    // Constructors
    public CountryDto() {
    }
    
    public CountryDto(Long id, String name, String shortCode, String logoUrl) {
        this.id = id;
        this.name = name;
        this.shortCode = shortCode;
        this.logoUrl = logoUrl;
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
    
    public String getShortCode() {
        return shortCode;
    }
    
    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }
    
    public String getLogoUrl() {
        return logoUrl;
    }
    
    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
