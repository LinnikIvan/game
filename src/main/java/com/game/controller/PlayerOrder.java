package com.game.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

public enum PlayerOrder {
    ID("id"), // default
    NAME("name"),
    EXPERIENCE("experience"),
    BIRTHDAY("birthday"),
    LEVEL("level");

    private final String fieldName;

    PlayerOrder(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }


//    получает список игроков по фильтру
//    @GetMapping("/players")
//    public ResponseEntity<Page<Player>> getPlayersListByFilter(@RequestParam Optional<Integer> pageNumber,
//                                                               @RequestParam Optional<Integer> pageSize) {
//
//        Page<Player> playerList = playerService.findAll(PageRequest.of(pageNumber.orElse(1),
//                pageSize.orElse(5)));
//
//        return new ResponseEntity<>(playerList, HttpStatus.OK);
//    }


}