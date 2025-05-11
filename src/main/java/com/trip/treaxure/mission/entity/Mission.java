package com.trip.treaxure.mission.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MISSION")
@Schema(description = "미션 정보를 나타내는 엔티티")
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_id")
    @Schema(description = "미션 고유 ID", example = "1")
    private Long missionId;

    @Column(name = "place_id", nullable = false)
    @Schema(description = "장소 ID", example = "101")
    private Long placeId;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "사용자 ID", example = "501")
    private Long userId;

    @Column(name = "title", length = 200, nullable = false)
    @Schema(description = "미션 제목", example = "서울타워 방문")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    @Schema(description = "미션 설명", example = "서울타워에 방문해 사진을 찍으세요.")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @Schema(description = "미션 타입", example = "PHOTO")
    private MissionType type = MissionType.PHOTO;

    @Column(name = "score", nullable = false)
    @Schema(description = "미션 점수", example = "10")
    private Integer score = 10;

    @Column(name = "reference_url", nullable = false)
    @Schema(description = "참고 이미지 URL", example = "https://example.com/mission.jpg")
    private String referenceUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Schema(description = "미션 상태", example = "PENDING")
    private MissionStatus status = MissionStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(description = "생성 시각", example = "2025-05-01T00:00:00")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "evaluated_at", nullable = false)
    @Schema(description = "평가 시각", example = "2025-05-02T00:00:00")
    private LocalDateTime evaluatedAt = LocalDateTime.now();

    @Column(name = "is_active", nullable = false)
    @Schema(description = "활성 상태", example = "true")
    private Boolean isActive = false;

    public enum MissionType {
        VISIT, PHOTO, POSE
    }

    public enum MissionStatus {
        PENDING, APPROVED, REJECTED
    }
} 