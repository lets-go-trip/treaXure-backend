package com.trip.xplore.place.service;

import com.trip.xplore.place.entity.Place;
import com.trip.xplore.place.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepository;

    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }

    public Optional<Place> getPlaceById(Long id) {
        return placeRepository.findById(id);
    }

    public Place createPlace(Place place) {
        return placeRepository.save(place);
    }

    public void deletePlace(Long id) {
        placeRepository.deleteById(id);
    }
} 