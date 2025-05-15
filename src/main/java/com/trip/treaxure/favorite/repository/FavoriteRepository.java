package com.trip.treaxure.favorite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trip.treaxure.favorite.entity.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    // TODO: Add custom queries if needed
} 