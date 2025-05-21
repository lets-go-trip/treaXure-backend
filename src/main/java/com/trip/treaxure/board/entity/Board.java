package com.trip.treaxure.board.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

import com.trip.treaxure.mission.entity.Mission;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "BOARD")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "게시글 정보를 나타내는 엔티티")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id", nullable = false)
    @Comment("게시글 고유 ID")
    @Schema(description = "게시글 고유 ID", example = "1")
    private Integer boardId;

    @Column(name = "member_id", nullable = false)
    @Comment("작성자 회원 ID")
    @Schema(description = "작성자 회원 ID", example = "101")
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    @Comment("미션 고유 ID")
    @Schema(description = "해당 게시글이 속한 미션", implementation = Mission.class)
    private Mission mission;

    @Column(name = "image_url", nullable = false)
    @Comment("저장된 사진 URL")
    @Schema(description = "사진의 저장 URL", example = "https://example.com/image.jpg")
    private String imageUrl;

    @Column(name = "favorite_count", nullable = false)
    @Comment("좋아요 개수")
    @Schema(description = "좋아요 수", example = "5")
    private Integer favoriteCount;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @Comment("업로드 시각")
    @Schema(description = "게시글 업로드 시각", example = "2025-05-11T13:45:00")
    private LocalDateTime createdAt;

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    @Comment("사진 삭제 여부")
    @Schema(description = "활성 상태 (true = 공개, false = 삭제)", example = "true")
    private Boolean isActive = true;

    @Column(name = "title")
    @Comment("게시글 제목")
    @Schema(description = "게시글 제목", example = "오늘의 여행 사진")
    private String title;
} 