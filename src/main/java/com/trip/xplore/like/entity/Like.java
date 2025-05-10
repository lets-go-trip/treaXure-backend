package com.trip.treaxure.like.entity;

import com.trip.treaxure.board.entity.Board;
import com.trip.treaxure.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "LIKE")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(LikeId.class)
public class Like {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Board post;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, comment = "좋아요한 시각")
    private LocalDateTime createdAt;
} 