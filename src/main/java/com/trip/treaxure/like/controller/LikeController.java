package com.trip.treaxure.like.controller;

import com.trip.treaxure.like.entity.Like;
import com.trip.treaxure.like.service.LikeService;

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
@RequestMapping("/api/likes")
@Tag(name = "Like API", description = "좋아요 관리를 위한 API")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Operation(summary = "전체 좋아요 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요 리스트 조회 성공")
    })
    @GetMapping
    public List<Like> getAllLikes() {
        return likeService.getAllLikes();
    }

    @Operation(summary = "좋아요 ID로 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요 조회 성공"),
            @ApiResponse(responseCode = "404", description = "좋아요를 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Like> getLikeById(@PathVariable Long id) {
        Optional<Like> like = likeService.getLikeById(id);
        return like.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "좋아요 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요 생성 성공")
    })
    @PostMapping
    public Like createLike(@RequestBody Like like) {
        return likeService.createLike(like);
    }

    @Operation(summary = "좋아요 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "좋아요 삭제 성공")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLike(@PathVariable Long id) {
        likeService.deleteLike(id);
        return ResponseEntity.noContent().build();
    }
} 