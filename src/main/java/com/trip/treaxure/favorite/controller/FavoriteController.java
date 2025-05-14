package com.trip.treaxure.favorite.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trip.treaxure.favorite.dto.request.FavoriteRequestDto;
import com.trip.treaxure.favorite.dto.response.FavoriteResponseDto;
import com.trip.treaxure.favorite.service.FavoriteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@Tag(name = "Favorite Controller", description = "좋아요 관련 API")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Operation(summary = "전체 좋아요 조회")
    @GetMapping
    public List<FavoriteResponseDto> getAllFavorites() {
        return favoriteService.getAllFavorites();
    }

    @Operation(summary = "좋아요 ID로 조회")
    @GetMapping("/{id}")
    public ResponseEntity<FavoriteResponseDto> getFavoriteById(@PathVariable Long id) {
        return favoriteService.getFavoriteById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "좋아요 생성")
    @PostMapping
    public FavoriteResponseDto createFavorite(@RequestBody FavoriteRequestDto dto) {
        return favoriteService.createFavorite(dto);
    }

    @Operation(summary = "좋아요 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long id) {
        favoriteService.deleteFavorite(id);
        return ResponseEntity.noContent().build();
    }
}
