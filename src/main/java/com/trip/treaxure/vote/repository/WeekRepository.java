package com.trip.treaxure.vote.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trip.treaxure.vote.entity.Week;

public interface WeekRepository extends JpaRepository<Week, Long> {
    // 필요 시 커스텀 쿼리 작성 가능
}
