package com.trip.treaxure.place.entity;

import com.trip.treaxure.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "VISIT")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visit_id", nullable = false, comment = "방문 기록 고유 ID")
    private Integer visitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "visit_count", nullable = false, columnDefinition = "INT DEFAULT 1", comment = "방문 횟수")
    private Integer visitCount;

    @Column(name = "visited_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP", comment = "체크인 시각")
    private LocalDateTime visitedAt;

    @Column(name = "left_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP", comment = "체크아웃 시각")
    private LocalDateTime leftAt;

    @Column(name = "duration_at", nullable = false, comment = "체류 시간")
    private LocalDateTime durationAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, comment = "생성 시각")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, comment = "수정 시각")
    private LocalDateTime updatedAt;
} 