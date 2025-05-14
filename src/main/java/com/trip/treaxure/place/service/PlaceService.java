package com.trip.treaxure.place.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.trip.treaxure.place.dto.request.PlaceRequestDto;
import com.trip.treaxure.place.dto.response.PlaceResponseDto;
import com.trip.treaxure.place.entity.Place;
import com.trip.treaxure.place.repository.PlaceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceRepository placeRepository;

    public List<PlaceResponseDto> getAllPlaces() {
        return placeRepository.findAll().stream()
                .map(PlaceResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public Optional<PlaceResponseDto> getPlaceById(Long id) {
        return placeRepository.findById(id)
                .map(PlaceResponseDto::fromEntity);
    }

    public PlaceResponseDto createPlace(PlaceRequestDto dto) {
        Place place = Place.builder()
                .name(dto.getName())
                .category(dto.getCategory())
                .description(dto.getDescription())
                .address(dto.getAddress())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .thumbnailUrl(dto.getThumbnailUrl())
                .isActive(true)
                .build();

        return PlaceResponseDto.fromEntity(placeRepository.save(place));
    }

    public void deletePlace(Long id) {
        placeRepository.deleteById(id);
    }
}
