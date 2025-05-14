package com.trip.treaxure.place.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PLACE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "장소 정보를 나타내는 엔티티")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Hibernate 프록시 무시
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id", nullable = false)
    @Comment("장소 고유 ID")
    @Schema(
        description = "장소 고유 ID",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY   // 읽기 전용
    )
    private Integer placeId;

    @Column(name = "name", nullable = false)
    @Comment("장소명")
    @Schema(description = "장소명", example = "서울타워")
    private String name;

    @Column(name = "category", nullable = false)
    @Comment("카테고리")
    @Schema(description = "카테고리", example = "관광지")
    private String category;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    @Comment("설명")
    @Schema(description = "장소 설명", example = "서울의 대표적인 관광지입니다.")
    private String description;

    @Column(name = "address", nullable = false)
    @Comment("주소")
    @Schema(description = "주소", example = "서울 중구 남산공원길 105")
    private String address;

    @Column(name = "latitude", nullable = false)
    @Comment("경도")
    @Schema(description = "경도", example = "37.5512")
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    @Comment("위도")
    @Schema(description = "위도", example = "126.9882")
    private Double longitude;

    @Column(name = "thumbnail_url")
    @Comment("대표 이미지 URL")
    @Schema(description = "대표 이미지 URL", example = "https://example.com/thumbnail.jpg")
    private String thumbnailUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @Comment("등록 시각")
    @Schema(
        description = "등록 시각",
        example = "2025-05-11T10:00:00",
        accessMode = Schema.AccessMode.READ_ONLY   // 읽기 전용
    )
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    @Comment("수정 시각")
    @Schema(
        description = "수정 시각",
        example = "2025-05-12T09:00:00",
        accessMode = Schema.AccessMode.READ_ONLY   // 읽기 전용
    )
    private LocalDateTime updatedAt;

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    @Comment("장소 활성화 여부")
    @Schema(description = "장소 활성 여부", example = "true")
    private Boolean isActive = true;
}
