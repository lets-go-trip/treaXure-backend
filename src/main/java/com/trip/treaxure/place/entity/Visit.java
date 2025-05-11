package com.trip.treaxure.place.entity;

import com.trip.treaxure.user.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "VISIT", 
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_user_place", columnNames = {"user_id", "place_id"})
       }
)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자의 장소 방문 기록을 나타내는 엔티티")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visit_id", nullable = false)
    @Comment("방문 기록 고유 ID")
    @Schema(description = "방문 기록 고유 ID", example = "1")
    private Integer visitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Comment("사용자 고유 ID")
    @Schema(description = "방문한 사용자", implementation = User.class)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    @Comment("장소 고유 ID")
    @Schema(description = "방문한 장소", implementation = Place.class)
    private Place place;

    @Column(name = "visit_count", nullable = false, columnDefinition = "INT DEFAULT 1")
    @Comment("방문 횟수")
    @Schema(description = "방문 횟수", example = "3")
    private Integer visitCount = 1;

    @Column(name = "visited_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Comment("체크인 시각")
    @Schema(description = "체크인 시각", example = "2025-05-11T08:00:00")
    private LocalDateTime visitedAt = LocalDateTime.now();

    @Column(name = "left_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Comment("체크아웃 시각")
    @Schema(description = "체크아웃 시각", example = "2025-05-11T09:30:00")
    private LocalDateTime leftAt = LocalDateTime.now();

    @Column(name = "duration_at", nullable = false)
    @Comment("체류 시간")
    @Schema(description = "체류 시간", example = "2025-05-11T01:30:00")
    private LocalDateTime durationAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @Comment("생성 시각")
    @Schema(description = "방문 기록 생성 시각", example = "2025-05-11T10:00:00")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    @Comment("수정 시각")
    @Schema(description = "방문 기록 수정 시각", example = "2025-05-12T09:00:00")
    private LocalDateTime updatedAt;
} 