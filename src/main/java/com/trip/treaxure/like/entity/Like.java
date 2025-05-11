package com.trip.treaxure.like.entity;

import com.trip.treaxure.board.entity.Board;
import com.trip.treaxure.user.entity.User;
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
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    @Comment("좋아요 고유 ID")
    private Long likeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @Comment("게시글 고유 ID")
    private Board post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Comment("좋아요한 사용자 ID")
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @Comment("좋아요한 시각")
    private LocalDateTime createdAt;
} 