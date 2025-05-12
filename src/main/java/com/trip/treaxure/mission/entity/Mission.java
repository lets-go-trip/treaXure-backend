package com.trip.treaxure.mission.entity;

import com.trip.treaxure.member.entity.Member;
import com.trip.treaxure.place.entity.Place;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

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

    @Comment("미션 고유 ID")
    @Schema(description = "미션 고유 ID", example = "1")
    private Long missionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    @Comment("장소 고유 ID")
    @Schema(description = "장소 ID", example = "101")
    private Place placeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @Comment("유저 고유 ID")
    @Schema(description = "사용자 ID", example = "501")
    private Member memberId;

    @Column(name = "title", length = 200, nullable = false)
    @Comment("미션 제목")
    @Schema(description = "미션 제목", example = "서울타워 방문")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    @Comment("상세 설명")
    @Schema(description = "미션 설명", example = "서울타워에 방문해 사진을 찍으세요.")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @Comment("심사 상태")
    @Schema(description = "미션 타입", example = "PHOTO")
    private MissionType type = MissionType.PHOTO;

    @Column(name = "score", nullable = false)
    @Comment("미션 점수")
    @Schema(description = "미션 점수", example = "10")
    private Integer score = 10;

    @Column(name = "reference_url", nullable = false)
    @Comment("예시 이미지 URL")
    @Schema(description = "참고 이미지 URL", example = "https://example.com/mission.jpg")
    private String referenceUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Comment("심사 상태")
    @Schema(description = "미션 상태", example = "PENDING")
    private MissionStatus status = MissionStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Comment("생성 시각")
    @Schema(description = "생성 시각", example = "2025-05-01T00:00:00")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "evaluated_at", nullable = false)
    @Comment("수정 시각")
    @Schema(description = "평가 시각", example = "2025-05-02T00:00:00")
    private LocalDateTime evaluatedAt = LocalDateTime.now();

    @Column(name = "is_active", nullable = false)
    @Comment("미션 활성화 여부")
    @Schema(description = "활성 상태", example = "true")
    private Boolean isActive = false;

    public enum MissionType {
        VISIT, PHOTO, POSE
    }

    public enum MissionStatus {
        PENDING, APPROVED, REJECTED
    }
} 