package com.trip.treaxure.board.entity;

import com.trip.treaxure.mission.entity.Mission;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "BOARD")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id", nullable = false)
    @Comment("게시글 고유 ID")
    private Integer boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    @Comment("미션 고유 ID")
    private Mission mission;

    @Column(name = "image_url", nullable = false)
    @Comment("저장된 사진 URL")
    private String imageUrl;

    @Column(name = "like_count", nullable = false)
    @Comment("좋아요 개수")
    private Integer likeCount;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @Comment("업로드 시각")
    private LocalDateTime createdAt;

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    @Comment("사진 삭제 여부")
    private Boolean isActive = true;

    @Column(name = "title")
    @Comment("게시글 제목")
    private String title;
} 