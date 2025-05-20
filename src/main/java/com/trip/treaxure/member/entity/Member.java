package com.trip.treaxure.member.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "MEMBER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "사용자 정보를 나타내는 엔티티")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    @Comment("사용자 고유 ID")
    @Schema(description = "사용자 고유 ID", example = "1")
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
    @Default
    private MemberRole role = MemberRole.USER;

    @Builder.Default
    @Column(name = "point", nullable = false, columnDefinition = "INT DEFAULT 0")
    @Comment("사용자 포인트")
    @Schema(description = "사용자 포인트", example = "0")
    private Integer point = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @Comment("가입 시각")
    @Schema(description = "가입 시각", example = "2025-05-11T12:00:00")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    @Comment("수정 시각")
    @Schema(description = "수정 시각", example = "2025-05-12T10:30:00")
    private LocalDateTime updatedAt;

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    @Comment("계정 활성화 여부")
    @Schema(description = "활성 여부", example = "true")
    @Default
    private Boolean isActive = true;

    @PrePersist
    protected void onCreate() {
        if (profileUrl == null) profileUrl = "https://thumb16.iclickart.co.kr/Thumb16/1170000/1166288.jpg";
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum MemberRole {
        ADMIN, USER
    }
} 