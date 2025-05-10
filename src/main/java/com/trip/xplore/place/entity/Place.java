package com.trip.treaxure.place.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "PLACE")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id", nullable = false, comment = "장소 고유 ID")
    private Integer placeId;

    @Column(name = "name", nullable = false, comment = "장소명")
    private String name;

    @Column(name = "category", nullable = false, comment = "카테고리")
    private String category;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT", comment = "설명")
    private String description;

    @Column(name = "address", nullable = false, comment = "주소")
    private String address;

    @Column(name = "latitude", nullable = false, comment = "경도")
    private Double latitude;

    @Column(name = "longitude", nullable = false, comment = "위도")
    private Double longitude;

    @Column(name = "thumbnail_url", comment = "대표 이미지 URL")
    private String thumbnailUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, comment = "등록 시각")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, comment = "수정 시각")
    private LocalDateTime updatedAt;

    @Column(name = "is_active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1", comment = "장소 활성화 여부")
    private Boolean isActive;
} 