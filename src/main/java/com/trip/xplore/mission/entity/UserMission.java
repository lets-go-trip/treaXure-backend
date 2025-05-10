package com.trip.treaxure.mission.entity;

import com.trip.treaxure.place.entity.Place;
import com.trip.treaxure.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "USER_MISSION")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_id", nullable = false, comment = "미션 고유 ID")
    private Integer missionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title", nullable = false, length = 200, comment = "미션 제목")
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT", comment = "상세 설명")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "ENUM('VISIT','board','POSE') DEFAULT 'board'", comment = "미션 유형")
    private MissionType type;

    @Column(name = "score", nullable = false, columnDefinition = "INT DEFAULT 10", comment = "미션 점수")
    private Integer score;

    @Column(name = "reference_url", nullable = false, comment = "예시 이미지 URL")
    private String referenceUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "ENUM('PENDING','APPROVED','REJECTED') DEFAULT 'PENDING'", comment = "심사 상태")
    private MissionStatus status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, comment = "생성 시각")
    private LocalDateTime createdAt;

    @Column(name = "evaluated_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP", comment = "심사 통과 시각")
    private LocalDateTime evaluatedAt;

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0", comment = "미션 활성화 여부")
    private Boolean isActive;

    public enum MissionType {
        VISIT, PHOTO, POSE
    }

    public enum MissionStatus {
        PENDING, APPROVED, REJECTED
    }
} 