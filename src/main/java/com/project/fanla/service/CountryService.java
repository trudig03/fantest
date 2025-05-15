package com.project.fanla.service;

import com.project.fanla.dto.CountryDto;
import com.project.fanla.entity.Country;
import com.project.fanla.repository.CountryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Transactional(readOnly = true)
    public List<CountryDto> getAllCountries() {
        return countryRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CountryDto getCountryById(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Country not found with id: " + id));
        return convertToDto(country);
    }

    @Transactional
    public CountryDto createCountry(CountryDto countryDto) {
        // Validate country doesn't already exist
        if (countryRepository.existsByName(countryDto.getName())) {
            throw new RuntimeException("Country with name '" + countryDto.getName() + "' already exists");
        }
        
        if (countryRepository.existsByShortCode(countryDto.getShortCode())) {
            throw new RuntimeException("Country with short code '" + countryDto.getShortCode() + "' already exists");
        }
        
        // Convert DTO to entity
        Country country = new Country();
        country.setName(countryDto.getName());
        country.setShortCode(countryDto.getShortCode());
        country.setLogoUrl(countryDto.getLogoUrl());
        
        // Save entity
        Country savedCountry = countryRepository.save(country);
        
        // Return DTO with generated ID
        return convertToDto(savedCountry);
    }

    @Transactional
    public CountryDto updateCountry(Long id, CountryDto countryDto) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Country not found with id: " + id));
        
        // Check if name is being changed and if new name already exists
        if (!country.getName().equals(countryDto.getName()) && 
                countryRepository.existsByName(countryDto.getName())) {
            throw new RuntimeException("Country with name '" + countryDto.getName() + "' already exists");
        }
        
        // Check if short code is being changed and if new short code already exists
        if (!country.getShortCode().equals(countryDto.getShortCode()) && 
                countryRepository.existsByShortCode(countryDto.getShortCode())) {
            throw new RuntimeException("Country with short code '" + countryDto.getShortCode() + "' already exists");
        }
        
        // Update entity
        country.setName(countryDto.getName());
        country.setShortCode(countryDto.getShortCode());
        country.setLogoUrl(countryDto.getLogoUrl());
        
        // Save updated entity
        Country updatedCountry = countryRepository.save(country);
        
        // Return updated DTO
        return convertToDto(updatedCountry);
    }

    @Transactional
    public void deleteCountry(Long id) {
        if (!countryRepository.existsById(id)) {
            throw new RuntimeException("Country not found with id: " + id);
        }
        
        countryRepository.deleteById(id);
    }
    
    // Helper method to convert entity to DTO
    private CountryDto convertToDto(Country country) {
        return new CountryDto(
                country.getId(),
                country.getName(),
                country.getShortCode(),
                country.getLogoUrl()
        );
    }
}
