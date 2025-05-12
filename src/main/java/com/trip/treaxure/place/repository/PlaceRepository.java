package com.trip.treaxure.place.repository;

import com.trip.treaxure.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {
    // TODO: Add custom queries if needed
} 