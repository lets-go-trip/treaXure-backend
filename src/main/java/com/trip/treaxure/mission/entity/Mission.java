package com.trip.treaxure.mission.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private Long missionId;

    @Column(name = "place_id", nullable = false)
    private Long placeId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private MissionType type = MissionType.PHOTO;

    @Column(name = "score", nullable = false)
    private Integer score = 10;

    @Column(name = "reference_url", nullable = false)
    private String referenceUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MissionStatus status = MissionStatus.PENDING;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "evaluated_at", nullable = false)
    private LocalDateTime evaluatedAt = LocalDateTime.now();

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

    public enum MissionType {
        VISIT, PHOTO, POSE
    }

    public enum MissionStatus {
        PENDING, APPROVED, REJECTED
    }
} 