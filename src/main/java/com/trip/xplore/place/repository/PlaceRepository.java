package com.trip.xplore.place.repository;

import com.trip.xplore.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    // TODO: Add custom queries if needed
} 