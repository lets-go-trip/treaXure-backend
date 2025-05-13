package com.trip.treaxure.member.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Entity
@Table(name = "MEMBER")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 정보를 나타내는 엔티티")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    @Comment("사용자 고유 ID")
    @Schema(description = "사용자 고유 ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer memberId;

    @Column(name = "email", nullable = false)
    @Comment("로그인용 이메일")
    @Schema(description = "사용자 이메일", example = "member@example.com")
    private String email;

    @Column(name = "password", nullable = false)
    @Comment("해시된 비밀번호")
    @Schema(description = "비밀번호(해시됨)", example = "$2a$10$...hashed")
    private String password;

    @Column(name = "nickname", nullable = false)
    @Comment("닉네임")
    @Schema(description = "사용자 닉네임", example = "트레져헌터")
    private String nickname;

    @Column(name = "profile_url")
    @Comment("프로필 이미지 URL")
    @Schema(description = "프로필 이미지 URL", example = "https://example.com/image.jpg")
    private String profileUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, columnDefinition = "ENUM('ADMIN','USER') DEFAULT 'USER'")
    @Comment("권한")
    @Schema(description = "사용자 역할", example = "USER")
    private MemberRole role = MemberRole.USER;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @Comment("가입 시각")
    @Schema(description = "가입 시각", example = "2025-05-11T12:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    @Comment("수정 시각")
    @Schema(description = "수정 시각", example = "2025-05-12T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    @Comment("계정 활성화 여부")
    @Schema(description = "활성 여부", example = "true")
    private Boolean isActive = true;

    @PrePersist
    protected void onCreate() {
        if (profileUrl == null) profileUrl = "기본_사용자_이미지_URL";
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum MemberRole {
        ADMIN, USER
    }

    public void updateProfile(String nickname, String profileUrl) {
        this.nickname = nickname;
        this.profileUrl = profileUrl;
    }
} 