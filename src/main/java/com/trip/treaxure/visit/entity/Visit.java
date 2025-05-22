package com.trip.treaxure.visit.entity;

import com.trip.treaxure.member.entity.Member;
import com.trip.treaxure.place.entity.Place;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import lombok.Builder.Default;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "VISIT", 
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_member_place", columnNames = {"member_id", "place_id"})
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
    @JoinColumn(name = "member_id", nullable = false)
    @Comment("사용자 고유 ID")
    @Schema(description = "방문한 사용자", implementation = Member.class)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    @Comment("장소 고유 ID")
    @Schema(description = "방문한 장소", implementation = Place.class)
    private Place place;

    @Column(name = "visit_count", nullable = false, columnDefinition = "INT DEFAULT 1")
    @Comment("방문 횟수")
    @Schema(description = "방문 횟수", example = "3")
    @Default
    private Integer visitCount = 1;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @Comment("첫 방문 시각")
    @Schema(description = "첫 방문 시각", example = "2025-05-11T10:00:00")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    @Comment("최근 방문 시각")
    @Schema(description = "최근 방문 시각", example = "2025-05-12T09:00:00")
    private LocalDateTime updatedAt;
} 