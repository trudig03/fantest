package com.project.fanla.repository;

import com.project.fanla.entity.Lyrics;
import com.project.fanla.entity.Sound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LyricsRepository extends JpaRepository<Lyrics, Long> {
    Optional<Lyrics> findBySound(Sound sound);
}
