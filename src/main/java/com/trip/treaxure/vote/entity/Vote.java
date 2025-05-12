package com.trip.treaxure.vote.entity;

import com.trip.treaxure.board.entity.Board;
import com.trip.treaxure.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.Comment;
@Table(name = "VOTE", 
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_week_member_board", columnNames = {"week_id", "member_id", "board_id"})
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
    @JoinColumn(name = "member_id", nullable = false)
    @Comment("투표한 사용자 고유 ID")
    private Member memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    @Comment("게시글 고유 ID")
    private Board boardId;

    @Column(name = "Field")
    @Comment("필드")
    private String field;
} 