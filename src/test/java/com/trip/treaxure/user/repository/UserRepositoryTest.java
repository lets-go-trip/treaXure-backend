package com.trip.treaxure.user.repository;

import com.trip.treaxure.user.entity.User;
import com.trip.treaxure.user.entity.User.UserRole;
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
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자 생성 및 조회 테스트")
    public void 사용자생성_및_조회() {
        // 사용자 생성
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("hashedPassword123");
        user.setNickname("테스트사용자");
        user.setProfileUrl("https://example.com/profile.jpg");
        user.setRole(UserRole.USER);
        user.setIsActive(true);

        // 사용자 저장
        User savedUser = userRepository.save(user);
        
        // 영속성 컨텍스트 초기화하여 데이터베이스에서 조회되도록 함
        entityManager.flush();
        entityManager.clear();

        // 사용자 조회
        User foundUser = userRepository.findById(savedUser.getId()).orElse(null);
        
        // 검증
        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
        assertEquals("hashedPassword123", foundUser.getPassword());
        assertEquals("테스트사용자", foundUser.getNickname());
        assertEquals("https://example.com/profile.jpg", foundUser.getProfileUrl());
        assertEquals(UserRole.USER, foundUser.getRole());
        assertTrue(foundUser.getIsActive());
        assertNotNull(foundUser.getCreatedAt());
        assertNotNull(foundUser.getUpdatedAt());
    }

    @Test
    @DisplayName("기본값 적용 테스트")
    public void 기본값_테스트() {
        // 최소한의 필수 필드만 설정한 사용자 생성
        User user = new User();
        user.setEmail("minimal@example.com");
        user.setPassword("password");
        user.setNickname("최소사용자");
        
        // 사용자 저장 - 기본값이 적용되어야 함
        User savedUser = userRepository.save(user);
        
        // 영속성 컨텍스트 초기화
        entityManager.flush();
        entityManager.clear();
        
        // 사용자 조회
        User foundUser = userRepository.findById(savedUser.getId()).orElse(null);
        
        // 기본값 검증
        assertNotNull(foundUser);
        assertEquals(UserRole.USER, foundUser.getRole());
        assertTrue(foundUser.getIsActive());
        assertNotNull(foundUser.getProfileUrl());
        assertNotNull(foundUser.getCreatedAt());
        assertNotNull(foundUser.getUpdatedAt());
    }

    @Test
    @DisplayName("사용자 정보 업데이트 테스트")
    public void 사용자_업데이트_테스트() {
        // 사용자 생성 및 저장
        User user = new User();
        user.setEmail("update@example.com");
        user.setPassword("initialPassword");
        user.setNickname("초기이름");
        user.setProfileUrl("https://example.com/initial.jpg");
        user.setRole(UserRole.USER);
        user.setIsActive(true);
        
        User savedUser = userRepository.save(user);
        LocalDateTime initialCreatedAt = savedUser.getCreatedAt();
        LocalDateTime initialUpdatedAt = savedUser.getUpdatedAt();
        
        // 시간 차이를 위해 잠시 대기
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 사용자 정보 업데이트
        savedUser.setNickname("변경된이름");
        savedUser.setProfileUrl("https://example.com/updated.jpg");
        savedUser.setRole(UserRole.ADMIN);
        User updatedUser = userRepository.save(savedUser);
        
        // 영속성 컨텍스트 초기화
        entityManager.flush();
        entityManager.clear();
        
        // 업데이트된 사용자 조회
        User foundUser = userRepository.findById(updatedUser.getId()).orElse(null);
        
        // 검증
        assertNotNull(foundUser);
        assertEquals("변경된이름", foundUser.getNickname());
        assertEquals("https://example.com/updated.jpg", foundUser.getProfileUrl());
        assertEquals(UserRole.ADMIN, foundUser.getRole());
        assertEquals(initialCreatedAt, foundUser.getCreatedAt()); // 생성 시각은 변경되지 않아야 함
        assertNotEquals(initialUpdatedAt, foundUser.getUpdatedAt()); // 업데이트 시각은 변경되어야 함
    }
    
    @Test
    @DisplayName("이메일로 사용자 찾기 테스트")
    public void 이메일로_사용자_찾기() {
        // 사용자 데이터 준비
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setPassword("password1");
        user1.setNickname("사용자1");
        userRepository.save(user1);
        
        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setPassword("password2");
        user2.setNickname("사용자2");
        userRepository.save(user2);
        
        entityManager.flush();
        entityManager.clear();
        
        // 커스텀 쿼리 메소드 실행
        Optional<User> foundUserOptional = userRepository.findByEmail("user1@example.com");
        
        // 검증
        assertTrue(foundUserOptional.isPresent());
        User foundUser = foundUserOptional.get();
        assertEquals("사용자1", foundUser.getNickname());
        assertEquals("user1@example.com", foundUser.getEmail());
    }
    
    @Test
    @DisplayName("존재하지 않는 이메일로 사용자 찾기 테스트")
    public void 존재하지_않는_이메일로_사용자_찾기() {
        // 사용자 데이터 준비
        User user = new User();
        user.setEmail("exists@example.com");
        user.setPassword("password");
        user.setNickname("존재사용자");
        userRepository.save(user);
        
        entityManager.flush();
        entityManager.clear();
        
        // 존재하지 않는 이메일로 조회
        Optional<User> foundUserOptional = userRepository.findByEmail("notexists@example.com");
        
        // 검증
        assertFalse(foundUserOptional.isPresent());
    }
    
    @Test
    @DisplayName("활성 사용자만 찾기 테스트")
    public void 활성_사용자_찾기() {
        // 활성 사용자 생성
        User activeUser = new User();
        activeUser.setEmail("active@example.com");
        activeUser.setPassword("password");
        activeUser.setNickname("활성사용자");
        activeUser.setIsActive(true);
        userRepository.save(activeUser);
        
        // 비활성 사용자 생성
        User inactiveUser = new User();
        inactiveUser.setEmail("inactive@example.com");
        inactiveUser.setPassword("password");
        inactiveUser.setNickname("비활성사용자");
        inactiveUser.setIsActive(false);
        userRepository.save(inactiveUser);
        
        entityManager.flush();
        entityManager.clear();
        
        // 커스텀 쿼리 메소드 실행
        List<User> activeUsers = userRepository.findByIsActiveTrue();
        
        // 검증
        assertNotNull(activeUsers);
        assertFalse(activeUsers.isEmpty());
        assertEquals(1, activeUsers.size());
        assertEquals("활성사용자", activeUsers.get(0).getNickname());
    }
    
    @Test
    @DisplayName("중복 이메일 제약조건 테스트")
    public void 중복_이메일_테스트() {
        // 첫 번째 사용자 생성
        User user1 = new User();
        user1.setEmail("duplicate@example.com");
        user1.setPassword("password1");
        user1.setNickname("사용자1");
        userRepository.save(user1);
        
        entityManager.flush();
        
        // 동일한 이메일로 두 번째 사용자 생성
        User user2 = new User();
        user2.setEmail("duplicate@example.com");
        user2.setPassword("password2");
        user2.setNickname("사용자2");
        
        // 예외 발생 여부 확인
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(user2);
            entityManager.flush();
        });
    }
    
    @Test
    @DisplayName("역할별 사용자 찾기 테스트")
    public void 역할별_사용자_찾기() {
        // 일반 사용자 생성
        User normalUser1 = new User();
        normalUser1.setEmail("user1@example.com");
        normalUser1.setPassword("password");
        normalUser1.setNickname("일반사용자1");
        normalUser1.setRole(UserRole.USER);
        userRepository.save(normalUser1);
        
        User normalUser2 = new User();
        normalUser2.setEmail("user2@example.com");
        normalUser2.setPassword("password");
        normalUser2.setNickname("일반사용자2");
        normalUser2.setRole(UserRole.USER);
        userRepository.save(normalUser2);
        
        // 관리자 사용자 생성
        User adminUser = new User();
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword("password");
        adminUser.setNickname("관리자");
        adminUser.setRole(UserRole.ADMIN);
        userRepository.save(adminUser);
        
        entityManager.flush();
        entityManager.clear();
        
        // 역할별 사용자 조회
        List<User> adminUsers = userRepository.findByRole(UserRole.ADMIN);
        List<User> normalUsers = userRepository.findByRole(UserRole.USER);
        
        // 검증
        assertEquals(1, adminUsers.size());
        assertEquals(2, normalUsers.size());
        assertEquals("관리자", adminUsers.get(0).getNickname());
    }
} 