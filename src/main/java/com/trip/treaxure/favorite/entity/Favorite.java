package com.trip.treaxure.favorite.entity;

import com.trip.treaxure.board.entity.Board;
import com.trip.treaxure.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Favorite", 
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_member_board", columnNames = {"board_id", "member_id"})
       }
)

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id")
    @Comment("좋아요 고유 ID")
    private Long favoriteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    @Comment("게시글 고유 ID")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @Comment("좋아요한 사용자 ID")
    private Member member;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @Comment("좋아요한 시각")
    private LocalDateTime createdAt;
} 