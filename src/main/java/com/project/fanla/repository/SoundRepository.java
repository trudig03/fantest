package com.project.fanla.repository;

import com.project.fanla.entity.Sound;
import com.project.fanla.entity.Team;
import com.project.fanla.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoundRepository extends JpaRepository<Sound, Long> {
    List<Sound> findByTeam(Team team);
    List<Sound> findByCreator(User creator);
}
