package com.trip.treaxure.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "USER")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, comment = "사용자 고유 ID")
    private Integer userId;

    @Column(name = "email", nullable = false, comment = "로그인용 이메일")
    private String email;

    @Column(name = "password", nullable = false, comment = "해시된 비밀번호")
    private String password;

    @Column(name = "nickname", nullable = false, comment = "닉네임")
    private String nickname;

    @Column(name = "profile_url", comment = "프로필 이미지 URL")
    private String profileUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, columnDefinition = "ENUM('ADMIN','USER') DEFAULT 'USER'", comment = "권한")
    private UserRole role;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, comment = "가입 시각")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, comment = "수정 시각")
    private LocalDateTime updatedAt;

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1", comment = "계정 활성화 여부")
    private Boolean isActive;

    @PrePersist
    protected void onCreate() {
        if (profileUrl == null) profileUrl = "기본_사용자_이미지_URL";
        if (role == null) role = UserRole.USER;
        if (isActive == null) isActive = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum UserRole {
        ADMIN, USER
    }
} 