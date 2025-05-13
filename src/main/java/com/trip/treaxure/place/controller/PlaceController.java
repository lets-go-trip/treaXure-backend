package com.trip.treaxure.place.controller;

import com.trip.treaxure.place.entity.Place;
import com.trip.treaxure.place.service.PlaceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/places")
@Tag(name = "Place Controller", description = "장소 관리를 위한 API")
public class PlaceController {

    @Autowired
    private PlaceService placeService;

    @Operation(summary = "전체 장소 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "장소 리스트 조회 성공")
    })
    @GetMapping
    public List<Place> getAllPlaces() {
        return placeService.getAllPlaces();
    }

    @Operation(summary = "장소 ID로 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "장소 조회 성공"),
            @ApiResponse(responseCode = "404", description = "장소를 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable Integer id) {
        Optional<Place> place = placeService.getPlaceById(id);
        return place.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "장소 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "장소 생성 성공")
    })
    @PostMapping
    public Place createPlace(@RequestBody Place place) {
        return placeService.createPlace(place);
    }

    @Operation(summary = "장소 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "장소 삭제 성공")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable Integer id) {
        placeService.deletePlace(id);
        return ResponseEntity.noContent().build();
    }
} 