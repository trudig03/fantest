package com.project.fanla.repository;

import com.project.fanla.entity.Match;
import com.project.fanla.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByTeam(Team team);
    List<Match> findByOpponentTeam(Team team);
}
