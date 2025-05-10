package com.trip.treaxure.photo.repository;

import com.trip.treaxure.photo.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    // TODO: Add custom queries if needed
} 