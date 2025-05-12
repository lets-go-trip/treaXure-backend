package com.trip.treaxure.member.repository;

import com.trip.treaxure.member.entity.Member;
import com.trip.treaxure.member.entity.Member.MemberRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("사용자 생성 및 조회 테스트")
    public void 사용자생성_및_조회() {
        // 사용자 생성
        Member member = new Member();
//        member.setEmail("test@example.com");
//        member.setPassword("hashedPassword123");
//        member.setNickname("테스트사용자");
//        member.setProfileUrl("https://example.com/profile.jpg");
//        member.setRole(MemberRole.MEMBER);
//        member.setIsActive(true);

        // 사용자 저장
        Member savedMember = memberRepository.save(member);
        
        // 영속성 컨텍스트 초기화하여 데이터베이스에서 조회되도록 함
        entityManager.flush();
        entityManager.clear();

        // 사용자 조회
//        Member foundMember = memberRepository.findById(savedMember.getMemberId()).orElse(null);
//
//        // 검증
//        assertNotNull(foundMember);
//        assertEquals("test@example.com", foundMember.getEmail());
//        assertEquals("hashedPassword123", foundMember.getPassword());
//        assertEquals("테스트사용자", foundMember.getNickname());
//        assertEquals("https://example.com/profile.jpg", foundMember.getProfileUrl());
//        assertEquals(MemberRole.MEMBER, foundMember.getRole());
//        assertTrue(foundMember.getIsActive());
//        assertNotNull(foundMember.getCreatedAt());
//        assertNotNull(foundMember.getUpdatedAt());
    }

    @Test
    @DisplayName("기본값 적용 테스트")
    public void 기본값_테스트() {
        // 최소한의 필수 필드만 설정한 사용자 생성
        Member member = new Member();
//        member.setEmail("minimal@example.com");
//        member.setPassword("password");
//        member.setNickname("최소사용자");
        
        // 사용자 저장 - 기본값이 적용되어야 함
        Member savedMember = memberRepository.save(member);
        
        // 영속성 컨텍스트 초기화
        entityManager.flush();
        entityManager.clear();
        
        // 사용자 조회
//        Member foundMember = memberRepository.findById(savedMember.getMemberId()).orElse(null);
        
        // 기본값 검증
//        assertNotNull(foundMember);
//        assertEquals(MemberRole.MEMBER, foundMember.getRole());
//        assertTrue(foundMember.getIsActive());
//        assertNotNull(foundMember.getProfileUrl());
//        assertNotNull(foundMember.getCreatedAt());
//        assertNotNull(foundMember.getUpdatedAt());
    }

    @Test
    @DisplayName("사용자 정보 업데이트 테스트")
    public void 사용자_업데이트_테스트() {
        // 사용자 생성 및 저장
        Member member = new Member();
//        member.setEmail("update@example.com");
//        member.setPassword("initialPassword");
//        member.setNickname("초기이름");
//        member.setProfileUrl("https://example.com/initial.jpg");
//        member.setRole(MemberRole.MEMBER);
//        member.setIsActive(true);
        
        Member savedMember = memberRepository.save(member);
        LocalDateTime initialCreatedAt = savedMember.getCreatedAt();
        LocalDateTime initialUpdatedAt = savedMember.getUpdatedAt();
        
        // 시간 차이를 위해 잠시 대기
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 사용자 정보 업데이트
//        savedMember.setNickname("변경된이름");
//        savedMember.setProfileUrl("https://example.com/updated.jpg");
//        savedMember.setRole(MemberRole.ADMIN);
        Member updatedMember = memberRepository.save(savedMember);
        
        // 영속성 컨텍스트 초기화
        entityManager.flush();
        entityManager.clear();
        
        // 업데이트된 사용자 조회
//        Member foundMember = memberRepository.findById(updatedMember.getMemberId()).orElse(null);
        
        // 검증
//        assertNotNull(foundMember);
//        assertEquals("변경된이름", foundMember.getNickname());
//        assertEquals("https://example.com/updated.jpg", foundMember.getProfileUrl());
//        assertEquals(MemberRole.ADMIN, foundMember.getRole());
//        assertEquals(initialCreatedAt, foundMember.getCreatedAt()); // 생성 시각은 변경되지 않아야 함
//        assertNotEquals(initialUpdatedAt, foundMember.getUpdatedAt()); // 업데이트 시각은 변경되어야 함
    }
    
    @Test
    @DisplayName("이메일로 사용자 찾기 테스트")
    public void 이메일로_사용자_찾기() {
        // 사용자 데이터 준비
        Member member1 = new Member();
//        member1.setEmail("member1@example.com");
//        member1.setPassword("password1");
//        member1.setNickname("사용자1");
        memberRepository.save(member1);
        
        Member member2 = new Member();
//        member2.setEmail("member2@example.com");
//        member2.setPassword("password2");
//        member2.setNickname("사용자2");
        memberRepository.save(member2);
        
        entityManager.flush();
        entityManager.clear();
        
        // 커스텀 쿼리 메소드 실행
        Optional<Member> foundMemberOptional = memberRepository.findByEmail("member1@example.com");
        
        // 검증
        assertTrue(foundMemberOptional.isPresent());
        Member foundMember = foundMemberOptional.get();
        assertEquals("사용자1", foundMember.getNickname());
        assertEquals("member1@example.com", foundMember.getEmail());
    }
    
    @Test
    @DisplayName("존재하지 않는 이메일로 사용자 찾기 테스트")
    public void 존재하지_않는_이메일로_사용자_찾기() {
        // 사용자 데이터 준비
        Member member = new Member();
//        member.setEmail("exists@example.com");
//        member.setPassword("password");
//        member.setNickname("존재사용자");
        memberRepository.save(member);
        
        entityManager.flush();
        entityManager.clear();
        
        // 존재하지 않는 이메일로 조회
        Optional<Member> foundMemberOptional = memberRepository.findByEmail("notexists@example.com");
        
        // 검증
        assertFalse(foundMemberOptional.isPresent());
    }
    
    @Test
    @DisplayName("활성 사용자만 찾기 테스트")
    public void 활성_사용자_찾기() {
        // 활성 사용자 생성
        Member activeMember = new Member();
//        activeMember.setEmail("active@example.com");
//        activeMember.setPassword("password");
//        activeMember.setNickname("활성사용자");
//        activeMember.setIsActive(true);
        memberRepository.save(activeMember);
        
        // 비활성 사용자 생성
        Member inactiveMember = new Member();
//        inactiveMember.setEmail("inactive@example.com");
//        inactiveMember.setPassword("password");
//        inactiveMember.setNickname("비활성사용자");
//        inactiveMember.setIsActive(false);
        memberRepository.save(inactiveMember);
        
        entityManager.flush();
        entityManager.clear();
        
        // 커스텀 쿼리 메소드 실행
        List<Member> activeMembers = memberRepository.findByIsActiveTrue();
        
        // 검증
        assertNotNull(activeMembers);
        assertFalse(activeMembers.isEmpty());
        assertEquals(1, activeMembers.size());
        assertEquals("활성사용자", activeMembers.get(0).getNickname());
    }
    
    @Test
    @DisplayName("중복 이메일 제약조건 테스트")
    public void 중복_이메일_테스트() {
        // 첫 번째 사용자 생성
        Member member1 = new Member();
//        member1.setEmail("duplicate@example.com");
//        member1.setPassword("password1");
//        member1.setNickname("사용자1");
        memberRepository.save(member1);
        
        entityManager.flush();
        
        // 동일한 이메일로 두 번째 사용자 생성
        Member member2 = new Member();
//        member2.setEmail("duplicate@example.com");
//        member2.setPassword("password2");
//        member2.setNickname("사용자2");
        
        // 예외 발생 여부 확인
        assertThrows(DataIntegrityViolationException.class, () -> {
            memberRepository.save(member2);
            entityManager.flush();
        });
    }
    
    @Test
    @DisplayName("역할별 사용자 찾기 테스트")
    public void 역할별_사용자_찾기() {
        // 일반 사용자 생성
        Member normalMember1 = new Member();
//        normalMember1.setEmail("member1@example.com");
//        normalMember1.setPassword("password");
//        normalMember1.setNickname("일반사용자1");
//        normalMember1.setRole(MemberRole.USER);
        memberRepository.save(normalMember1);
        
        Member normalMember2 = new Member();
//        normalMember2.setEmail("member2@example.com");
//        normalMember2.setPassword("password");
//        normalMember2.setNickname("일반사용자2");
//        normalMember2.setRole(MemberRole.USER);
        memberRepository.save(normalMember2);
        
        // 관리자 사용자 생성
        Member adminMember = new Member();
//        adminMember.setEmail("admin@example.com");
//        adminMember.setPassword("password");
//        adminMember.setNickname("관리자");
//        adminMember.setRole(MemberRole.ADMIN);
        memberRepository.save(adminMember);
        
        entityManager.flush();
        entityManager.clear();
        
        // 역할별 사용자 조회
        List<Member> adminMembers = memberRepository.findByRole(MemberRole.ADMIN);
        List<Member> normalMembers = memberRepository.findByRole(MemberRole.USER);
        
        // 검증
        assertEquals(1, adminMembers.size());
        assertEquals(2, normalMembers.size());
        assertEquals("관리자", adminMembers.get(0).getNickname());
    }
} 