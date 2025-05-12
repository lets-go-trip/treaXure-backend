package com.trip.treaxure.mission.entity;

import com.trip.treaxure.member.entity.Member;
import com.trip.treaxure.place.entity.Place;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MISSION")
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_id")
    @Comment("미션 고유 ID")
    private Long missionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    @Comment("장소 고유 ID")
    private Place placeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @Comment("유저 고유 ID")
    private Member memberId;

    @Column(name = "title", length = 200, nullable = false)
    @Comment("미션 제목")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    @Comment("상세 설명")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @Comment("심사 상태")
    private MissionType type = MissionType.PHOTO;

    @Column(name = "score", nullable = false)
    @Comment("미션 점수")
    private Integer score = 10;

    @Column(name = "reference_url", nullable = false)
    @Comment("예시 이미지 URL")
    private String referenceUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Comment("심사 상태")
    private MissionStatus status = MissionStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Comment("생성 시각")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "evaluated_at", nullable = false)
    @Comment("수정 시각")
    private LocalDateTime evaluatedAt = LocalDateTime.now();

    @Column(name = "is_active", nullable = false)
    @Comment("미션 활성화 여부")
    private Boolean isActive = false;

    public enum MissionType {
        VISIT, PHOTO, POSE
    }

    public enum MissionStatus {
        PENDING, APPROVED, REJECTED
    }
} 