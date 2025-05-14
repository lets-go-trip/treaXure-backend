package com.trip.treaxure.place.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trip.treaxure.global.dto.ApiResponseDto;
import com.trip.treaxure.place.dto.request.PlaceRequestDto;
import com.trip.treaxure.place.dto.response.PlaceResponseDto;
import com.trip.treaxure.place.service.PlaceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
@Tag(name = "Place Controller", description = "장소 관리를 위한 API")
public class PlaceController {

    private final PlaceService placeService;

    @Operation(summary = "전체 장소 조회")
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<PlaceResponseDto>>> getAllPlaces() {
        return ResponseEntity.ok(ApiResponseDto.success(placeService.getAllPlaces()));
    }

    @Operation(summary = "장소 ID로 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<PlaceResponseDto>> getPlaceById(@PathVariable Long id) {
        return placeService.getPlaceById(id)
                .map(ApiResponseDto::success)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "장소 등록")
    @PostMapping
    public ResponseEntity<ApiResponseDto<PlaceResponseDto>> createPlace(@RequestBody PlaceRequestDto dto) {
        return ResponseEntity.ok(ApiResponseDto.success(placeService.createPlace(dto)));
    }

    @Operation(summary = "장소 정보 수정")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "장소 수정 성공"),
        @ApiResponse(responseCode = "404", description = "장소를 찾을 수 없음")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Place> updatePlace(
            @PathVariable Long id,
            @RequestBody Place place) {
        Optional<Place> updated = placeService.updatePlace(id, place);
        return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "장소 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<Void>> deletePlace(@PathVariable Long id) {
        placeService.deletePlace(id);
        return ResponseEntity.ok(ApiResponseDto.success(null));
    }
}
