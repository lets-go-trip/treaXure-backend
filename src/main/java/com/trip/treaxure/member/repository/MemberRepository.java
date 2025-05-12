package com.trip.treaxure.member.repository;

import com.trip.treaxure.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    /**
     * 이메일로 사용자 찾기
     * @param email 찾을 사용자의 이메일
     * @return 찾은 사용자 Optional
     */
    Optional<Member> findByEmail(String email);
    
    /**
     * 활성 상태인 사용자만 조회
     * @return 활성 상태인 사용자 목록
     */
    List<Member> findByIsActiveTrue();
    
    /**
     * 닉네임으로 사용자 찾기
     * @param nickname 찾을 사용자의 닉네임
     * @return 찾은 사용자 Optional
     */
    Optional<Member> findByNickname(String nickname);
    
    /**
     * 특정 역할을 가진 사용자 조회
     * @param role 조회할 역할
     * @return 해당 역할을 가진 사용자 목록
     */
    List<Member> findByRole(Member.MemberRole role);
} 