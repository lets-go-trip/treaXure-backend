package com.trip.treaxure.place.entity;

import com.trip.treaxure.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
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
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visit_id", nullable = false)
    @Comment("방문 기록 고유 ID")
    private Integer visitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @Comment("사용자 고유 ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    @Comment("장소 고유 ID")
    private Place place;

    @Column(name = "visit_count", nullable = false, columnDefinition = "INT DEFAULT 1")
    @Comment("방문 횟수")
    private Integer visitCount = 1;

    @Column(name = "visited_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Comment("체크인 시각")
    private LocalDateTime visitedAt = LocalDateTime.now();

    @Column(name = "left_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Comment("체크아웃 시각")
    private LocalDateTime leftAt = LocalDateTime.now();

    @Column(name = "duration_at", nullable = false)
    @Comment("체류 시간")
    private LocalDateTime durationAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @Comment("생성 시각")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    @Comment("수정 시각")
    private LocalDateTime updatedAt;
} 