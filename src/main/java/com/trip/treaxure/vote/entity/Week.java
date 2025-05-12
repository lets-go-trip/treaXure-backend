package com.trip.treaxure.vote.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;

@Entity
@Table(name = "WEEK")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Week {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "week_id", nullable = false)
    @Comment("주 고유 ID")
    private Integer weekId;

    @Column(name = "week_start", nullable = false)
    @Comment("주 시작 날짜")
    private LocalDate weekStart;

    @Column(name = "week_end", nullable = false)
    @Comment("주 종료 날짜")
    private LocalDate weekEnd;
} 