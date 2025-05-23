package com.trip.treaxure.mission.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.trip.treaxure.board.entity.Board;
import com.trip.treaxure.mission.entity.Mission;
import com.trip.treaxure.mission.repository.MissionRepository;
import com.trip.treaxure.member.entity.Member;
import com.trip.treaxure.place.entity.Place;

@ExtendWith(MockitoExtension.class)
class MissionServiceTest {

    @Mock
    private MissionRepository missionRepository;

    @InjectMocks
    private MissionService missionService;

    private Mission testMission;
    private Board testBoard;
    private Member testMember;
    private Place testPlace;

    @BeforeEach
    void setUp() {
        // 테스트용 Member 생성
        testMember = Member.builder()
                .memberId(1L)
                .build();

        // 테스트용 Place 생성
        testPlace = Place.builder()
                .placeId(1)
                .build();

        // 테스트용 Mission 생성
        testMission = Mission.builder()
                .member(testMember)
                .place(testPlace)
                .title("테스트 미션")
                .description("테스트 미션 설명")
                .type(Mission.MissionType.PHOTO)
                .score(10)
                .referenceUrl("https://example.com/reference.jpg")
                .status(Mission.MissionStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .evaluatedAt(LocalDateTime.now())
                .isActive(true)
                .build();

        // 테스트용 Board 생성
        testBoard = Board.builder()
                .mission(testMission)
                .imageUrl("https://example.com/upload.jpg")
                .favoriteCount(0)
                .title("테스트 게시글")
                .isActive(true)
                .build();
    }

    // 여기에 MissionService의 다른 메서드에 대한 테스트를 추가할 수 있습니다.
} 