package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/rest")
public class PlayerController {

    private PlayerService playerService;

    @Autowired
    public void setPlayerService(PlayerService playerService) {
        this.playerService = playerService;
    }

    private boolean invalid(String id) {
        // Не валидным считается id, если он:
        // - не числовой
        // - не целое число
        // - не положительное число
        try {
            long playerId = Long.parseLong(id);
            if (playerId <= 0) {
                return true;
            }
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
    }

    @GetMapping("/players")
    @ResponseStatus(HttpStatus.OK)
    public List<Player> getPlayersList(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) Race race,
            @RequestParam(value = "profession", required = false) Profession profession,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
            @RequestParam(value = "order", required = false, defaultValue = "ID") PlayerOrder order,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));

        return playerService.getPlayersList(
                Specification.where(
                        playerService.nameFilter(name)
                                .and(playerService.titleFilter(title)))
                        .and(playerService.raceFilter(race))
                        .and(playerService.professionFilter(profession))
                        .and(playerService.birthdayFilter(after, before))
                        .and(playerService.bannedFilter(banned))
                        .and(playerService.experienceFilter(minExperience, maxExperience))
                        .and(playerService.levelFilter(minLevel, maxLevel)), pageable)
                .getContent();
    }

    @GetMapping("/players/count")
    @ResponseStatus(HttpStatus.OK)
    public Integer getPlayersCount(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) Race race,
            @RequestParam(value = "profession", required = false) Profession profession,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel) {

        return playerService.getPlayersList(
                Specification.where(
                        playerService.nameFilter(name)
                                .and(playerService.titleFilter(title)))
                        .and(playerService.raceFilter(race))
                        .and(playerService.professionFilter(profession))
                        .and(playerService.birthdayFilter(after, before))
                        .and(playerService.bannedFilter(banned))
                        .and(playerService.experienceFilter(minExperience, maxExperience))
                        .and(playerService.levelFilter(minLevel, maxLevel))
        ).size();
    }

    @PostMapping("/players")
    public ResponseEntity<?> createPlayer(@RequestBody Player requestPlayer) {
        Player responsePlayer = playerService.createPlayer(requestPlayer);
        if (responsePlayer == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else return new ResponseEntity<>(responsePlayer, HttpStatus.OK);
    }

    @GetMapping("/players/{id}")
    public ResponseEntity<Player> getPlayer(@PathVariable(name = "id") String id) {
        if (invalid(id)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Player player = playerService.getPlayer(Long.valueOf(id));
        if (player == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @PostMapping("/players/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable("id") String id, @RequestBody Player requestPlayer) {
        if (invalid(id)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (requestPlayer.getExperience() != null) {
            if (requestPlayer.getExperience() < 0 || requestPlayer.getExperience() > 10000000) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        Player responsePlayer = playerService.updatePlayer(Long.valueOf(id), requestPlayer);
        if (responsePlayer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else return new ResponseEntity<>(responsePlayer, HttpStatus.OK);
    }

    @DeleteMapping("/players/{id}")
    public ResponseEntity<?> deletePlayer(@PathVariable("id") String id) {
        if (invalid(id)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (playerService.deletePlayer(Long.valueOf(id))) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}