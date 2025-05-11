package com.trip.treaxure.like.entity;

import com.trip.treaxure.board.entity.Board;
import com.trip.treaxure.user.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "LIKE_TB", 
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_user_post", columnNames = {"post_id", "user_id"})
       }
)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "좋아요 정보를 나타내는 엔티티")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    @Comment("좋아요 고유 ID")
    @Schema(description = "좋아요 고유 ID", example = "1")
    private Long likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @Comment("게시글 고유 ID")
    @Schema(description = "좋아요를 누른 게시글", implementation = Board.class)
    private Board post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Comment("좋아요한 사용자 ID")
    @Schema(description = "좋아요한 사용자", implementation = User.class)
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @Comment("좋아요한 시각")
    @Schema(description = "좋아요 생성 시각", example = "2025-05-11T14:00:00")
    private LocalDateTime createdAt;
} 