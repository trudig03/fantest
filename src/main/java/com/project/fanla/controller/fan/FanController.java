package com.project.fanla.controller.fan;

import com.project.fanla.dto.MatchDto;
import com.project.fanla.entity.Country;
import com.project.fanla.entity.Team;
import com.project.fanla.repository.CountryRepository;
import com.project.fanla.repository.TeamRepository;
import com.project.fanla.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Fan uygulaması için tüm API endpoint'lerini içeren controller
 * Tüm endpoint'ler permitAll olarak yapılandırılmıştır ve kimlik doğrulama gerektirmez
 */
@RestController
@RequestMapping("/api/fan")
@CrossOrigin
public class FanController {

    private final CountryRepository countryRepository;
    private final TeamRepository teamRepository;
    private final MatchService matchService;

    @Autowired
    public FanController(CountryRepository countryRepository, 
                        TeamRepository teamRepository, 
                        MatchService matchService) {
        this.countryRepository = countryRepository;
        this.teamRepository = teamRepository;
        this.matchService = matchService;
    }

    /**
     * Tüm ülkeleri getirir
     * 
     * @return Ülke listesi
     * 
     * Örnek yanıt:
     * [
     *   {
     *     "id": 1,
     *     "name": "Türkiye",
     *     "shortCode": "TR",
     *     "logoUrl": "https://example.com/images/turkey-flag.png"
     *   }
     * ]
     */
    @GetMapping("/countries")
    public ResponseEntity<List<Country>> getAllCountries() {
        List<Country> countries = countryRepository.findAll();
        return ResponseEntity.ok(countries);
    }

    /**
     * Belirli bir ülkeye ait takımları getirir
     * 
     * @param countryId Ülke ID
     * @return Takım listesi
     * 
     * Örnek yanıt:
     * [
     *   {
     *     "id": 1,
     *     "name": "Fenerbahçe",
     *     "logo": "https://example.com/images/fenerbahce-logo.png",
     *     "description": "Fenerbahçe Spor Kulübü",
     *     "countryId": 1,
     *     "countryName": "Türkiye"
     *   }
     * ]
     */
    @GetMapping("/countries/{countryId}/teams")
    public ResponseEntity<List<Team>> getTeamsByCountry(@PathVariable Long countryId) {
        return countryRepository.findById(countryId)
                .map(country -> {
                    List<Team> teams = teamRepository.findByCountry(country);
                    return ResponseEntity.ok(teams);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Belirli bir takıma ait maçları getirir
     * Bu endpoint, takımın hem ev sahibi hem de deplasman olduğu maçları içerir
     * 
     * @param teamId Takım ID
     * @return Maç listesi
     * 
     * Örnek yanıt:
     * [
     *   {
     *     "id": 1,
     *     "name": "Fenerbahçe vs Galatasaray",
     *     "teamId": 1,
     *     "teamName": "Fenerbahçe",
     *     "opponentTeamId": 2,
     *     "opponentTeamName": "Galatasaray",
     *     "location": "Şükrü Saracoğlu Stadium",
     *     "matchDate": "2025-05-20T19:00:00",
     *     "homeScore": 2,
     *     "awayScore": 1,
     *     "status": "LIVE",
     *     "activeSoundId": 5,
     *     "activeSoundTitle": "Fenerbahçe Anthem",
     *     "activeSoundUrl": "https://example.com/sounds/fenerbahce-anthem.mp3",
     *     "soundStartTime": "2025-05-20T19:15:00",
     *     "elapsedTimeOnPause": null
     *   }
     * ]
     */
    @GetMapping("/teams/{teamId}/matches")
    @Transactional(readOnly = true)
    public ResponseEntity<List<MatchDto>> getMatchesByTeam(@PathVariable Long teamId) {
        try {
            List<MatchDto> matches = matchService.getMatchesByTeam(teamId);
            return ResponseEntity.ok(matches);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Tüm maçları getirir
     * 
     * @return Maç listesi
     * 
     * Örnek yanıt:
     * [
     *   {
     *     "id": 1,
     *     "name": "Fenerbahçe vs Galatasaray",
     *     "teamId": 1,
     *     "teamName": "Fenerbahçe",
     *     "opponentTeamId": 2,
     *     "opponentTeamName": "Galatasaray",
     *     "location": "Şükrü Saracoğlu Stadium",
     *     "matchDate": "2025-05-20T19:00:00",
     *     "homeScore": 2,
     *     "awayScore": 1,
     *     "status": "LIVE",
     *     "activeSoundId": 5,
     *     "activeSoundTitle": "Fenerbahçe Anthem",
     *     "activeSoundUrl": "https://example.com/sounds/fenerbahce-anthem.mp3",
     *     "soundStartTime": "2025-05-20T19:15:00",
     *     "elapsedTimeOnPause": null
     *   }
     * ]
     */
    @GetMapping("/matches")
    @Transactional(readOnly = true)
    public ResponseEntity<List<MatchDto>> getAllMatches() {
        List<MatchDto> matches = matchService.getAllMatches();
        return ResponseEntity.ok(matches);
    }

    /**
     * Belirli bir maçın detaylarını getirir
     * 
     * @param matchId Maç ID
     * @return Maç detayları
     * 
     * Örnek yanıt:
     * {
     *   "id": 1,
     *   "name": "Fenerbahçe vs Galatasaray",
     *   "teamId": 1,
     *   "teamName": "Fenerbahçe",
     *   "opponentTeamId": 2,
     *   "opponentTeamName": "Galatasaray",
     *   "location": "Şükrü Saracoğlu Stadium",
     *   "matchDate": "2025-05-20T19:00:00",
     *   "homeScore": 2,
     *   "awayScore": 1,
     *   "status": "LIVE",
     *   "activeSoundId": 5,
     *   "activeSoundTitle": "Fenerbahçe Anthem",
     *   "activeSoundUrl": "https://example.com/sounds/fenerbahce-anthem.mp3",
     *   "soundStartTime": "2025-05-20T19:15:00",
     *   "elapsedTimeOnPause": null
     * }
     */
    @GetMapping("/matches/{matchId}")
    @Transactional(readOnly = true)
    public ResponseEntity<MatchDto> getMatchById(@PathVariable Long matchId) {
        try {
            MatchDto matchDto = matchService.getMatchById(matchId);
            return ResponseEntity.ok(matchDto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
