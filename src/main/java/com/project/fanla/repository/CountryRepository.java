package com.project.fanla.repository;

import com.project.fanla.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    boolean existsByName(String name);
    boolean existsByShortCode(String shortCode);
    Optional<Country> findByName(String name);
}
