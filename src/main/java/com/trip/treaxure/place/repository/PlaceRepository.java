package com.trip.treaxure.place.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trip.treaxure.place.entity.Place;

/**
 * 장소 관련 DB 작업을 처리하는 JPA 리포지토리
 */
public interface PlaceRepository extends JpaRepository<Place, Integer> {

    /**
     * 카테고리로 장소 목록 조회
     *
     * @param category 장소 카테고리
     * @return 해당 카테고리의 장소 목록
     */
    List<Place> findByCategory(String category);

    /**
     * 이름 키워드를 포함한 장소 조회
     *
     * @param keyword 장소 이름 키워드
     * @return 키워드를 포함하는 장소 목록
     */
    List<Place> findByNameContainingIgnoreCase(String keyword);

    /**
     * 활성화된 장소 조회
     *
     * @return 활성화된 장소 목록
     */
    List<Place> findByIsActiveTrue();
}