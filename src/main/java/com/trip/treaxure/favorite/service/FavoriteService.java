package com.trip.treaxure.favorite.service;

import com.trip.treaxure.favorite.entity.Favorite;
import com.trip.treaxure.favorite.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    public List<Favorite> getAllfavorites() {
        return favoriteRepository.findAll();
    }

    public Optional<Favorite> getfavoriteById(Long id) {
        return favoriteRepository.findById(id);
    }

    public Favorite createfavorite(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    public void deletefavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
} 