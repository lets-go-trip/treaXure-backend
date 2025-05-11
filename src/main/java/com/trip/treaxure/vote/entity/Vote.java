package com.trip.treaxure.vote.entity;

import com.trip.treaxure.board.entity.Board;
import com.trip.treaxure.user.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.Comment;
@Table(name = "VOTE", 
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_week_user_post", columnNames = {"week_id", "user_id", "post_id"})
       }
)
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자의 게시글 투표 정보를 나타내는 엔티티")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id", nullable = false)
    @Comment("투표 고유 ID")
    @Schema(description = "투표 고유 ID", example = "1")
    private Long voteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "week_id", nullable = false)
    @Comment("주 고유 ID")
    @Schema(description = "해당 투표가 속한 주 정보", implementation = Week.class)
    private Week weekId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Comment("투표한 사용자 고유 ID")
    @Schema(description = "투표한 사용자 정보", implementation = User.class)
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @Comment("게시글 고유 ID")
    @Schema(description = "투표 대상 게시글", implementation = Board.class)
    private Board postId;

    @Column(name = "Field")
    @Comment("필드")
    @Schema(description = "기타 필드", example = "best")
    private String field;
} 