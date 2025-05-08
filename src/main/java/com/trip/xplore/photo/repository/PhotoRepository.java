package com.trip.xplore.photo.repository;

import com.trip.xplore.photo.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    // TODO: Add custom queries if needed
} 