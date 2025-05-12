package com.trip.treaxure.favorite.controller;

import com.trip.treaxure.favorite.entity.Favorite;
import com.trip.treaxure.favorite.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @GetMapping
    public List<Favorite> getAllfavorites() {
        return favoriteService.getAllfavorites();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Favorite> getfavoriteById(@PathVariable Long id) {
        Optional<Favorite> favorite = favoriteService.getfavoriteById(id);
        return favorite.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Favorite createfavorite(@RequestBody Favorite favorite) {
        return favoriteService.createfavorite(favorite);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletefavorite(@PathVariable Long id) {
        favoriteService.deletefavorite(id);
        return ResponseEntity.noContent().build();
    }
} 