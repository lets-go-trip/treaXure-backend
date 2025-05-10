package com.trip.treaxure.vote.entity;

import com.trip.treaxure.board.entity.Board;
import com.trip.treaxure.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "WEEKLY_board")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Weeklyboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id", nullable = false, comment = "투표 고유 ID")
    private Integer voteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "week_id", nullable = false)
    private Week week;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Board post;
} 