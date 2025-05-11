package com.trip.treaxure.vote.entity;

import com.trip.treaxure.board.entity.Board;
import com.trip.treaxure.user.entity.User;
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
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id", nullable = false)
    @Comment("투표 고유 ID")
    private Long voteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "week_id", nullable = false)
    @Comment("주 고유 ID")
    private Week weekId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Comment("투표한 사용자 고유 ID")
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @Comment("게시글 고유 ID")
    private Board postId;

    @Column(name = "Field")
    @Comment("필드")
    private String field;
} 