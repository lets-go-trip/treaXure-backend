package com.trip.treaxure.visit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trip.treaxure.member.entity.Member;
import com.trip.treaxure.place.entity.Place;
import com.trip.treaxure.visit.entity.Visit;

public interface VisitRepository extends JpaRepository<Visit, Integer> {
    Optional<Visit> findByMemberAndPlace(Member member, Place place);
    List<Visit> findByPlace(Place place);
    List<Visit> findByMember(Member member);
}
