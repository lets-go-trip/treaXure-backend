package com.trip.treaxure.place.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trip.treaxure.place.entity.Place;
import com.trip.treaxure.place.repository.PlaceRepository;

/**
 * 장소 관련 비즈니스 로직 처리 서비스
 */
@Service
public class PlaceService {

    @Autowired
    private PlaceRepository placeRepository;

    /**
     * 모든 장소 조회
     *
     * @return 전체 장소 목록
     */
    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }

    /**
     * ID로 장소 조회
     *
     * @param id 장소 ID
     * @return 장소 Optional
     */
    public Optional<Place> getPlaceById(Long id) {
        return placeRepository.findById(id);
    }

    /**
     * 장소 등록
     *
     * @param place 등록할 장소
     * @return 저장된 장소
     */
    public Place createPlace(Place place) {
        return placeRepository.save(place);
    }

    /**
     * 장소 삭제
     *
     * @param id 삭제할 장소 ID
     */
    public void deletePlace(Long id) {
        placeRepository.deleteById(id);
    }
}
