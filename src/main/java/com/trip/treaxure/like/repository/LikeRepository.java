package com.trip.treaxure.like.repository;

import com.trip.treaxure.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    // TODO: Add custom queries if needed
} 