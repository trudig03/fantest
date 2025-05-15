package com.project.fanla.repository;

import com.project.fanla.entity.Country;
import com.project.fanla.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    boolean existsByName(String name);
    Optional<Team> findByName(String name);
    List<Team> findByCountry(Country country);
}
