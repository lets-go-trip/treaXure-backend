package com.trip.treaxure.favorite.repository;

import com.trip.treaxure.favorite.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    // TODO: Add custom queries if needed
} 