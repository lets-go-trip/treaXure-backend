package com.trip.treaxure.board.entity;

import com.trip.treaxure.mission.entity.UserMission;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "BOARDS")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id", nullable = false, comment = "게시글 고유 ID")
    private Integer boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private UserMission mission;

    @Column(name = "image_url", nullable = false, comment = "저장된 사진 URL")
    private String imageUrl;

    @Column(name = "like_count", nullable = false, comment = "좋아요 개수")
    private Integer likeCount;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, comment = "업로드 시각")
    private LocalDateTime createdAt;

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1", comment = "사진 삭제 여부")
    private Boolean isActive;

    @Column(name = "title", comment = "게시글 제목")
    private String title;
} 