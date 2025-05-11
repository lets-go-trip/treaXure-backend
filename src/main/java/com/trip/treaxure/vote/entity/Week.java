package com.trip.treaxure.vote.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Entity
@Table(name = "WEEK")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "일주일 단위 주차 정보를 나타내는 엔티티")
public class Week {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "week_id", nullable = false)
    @Comment("주 고유 ID")
    @Schema(description = "주 고유 ID", example = "1")
    private Integer weekId;

    @Column(name = "week_start", nullable = false)
    @Comment("주 시작 날짜")
    @Schema(description = "주 시작일", example = "2025-05-05")
    private LocalDate weekStart;

    @Column(name = "week_end", nullable = false)
    @Comment("주 종료 날짜")
    @Schema(description = "주 종료일", example = "2025-05-11")
    private LocalDate weekEnd;
} 