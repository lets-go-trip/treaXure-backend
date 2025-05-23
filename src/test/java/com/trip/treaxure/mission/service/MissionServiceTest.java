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
import com.trip.treaxure.board.repository.BoardRepository;
import com.trip.treaxure.mission.entity.Mission;
import com.trip.treaxure.mission.repository.MissionRepository;
import com.trip.treaxure.member.entity.Member;
import com.trip.treaxure.place.entity.Place;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class MissionServiceTest {

    @Mock
    private MissionRepository missionRepository;

    @Mock
    private BoardRepository boardRepository;

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

    @Test
    void evaluateImageSimilarity_Success() {
        // Given
        when(missionRepository.findById(1L)).thenReturn(Optional.of(testMission));
        when(boardRepository.findById(1L)).thenReturn(Optional.of(testBoard));
        when(boardRepository.save(any(Board.class))).thenAnswer(invocation -> {
            Board savedBoard = invocation.getArgument(0);
            assertNotNull(savedBoard.getSimilarityScore(), "유사도 점수가 저장되어야 합니다");
            return savedBoard;
        });

        // When
        Float score = missionService.evaluateImageSimilarity(1L, 1L);

        // Then
        assertNotNull(score, "유사도 점수가 null이 아니어야 합니다");
        assertTrue(score >= 0 && score <= 1, "유사도 점수는 0과 1 사이여야 합니다");
        verify(boardRepository).save(argThat(board -> 
            board.getSimilarityScore() != null && 
            board.getSimilarityScore().equals(score)
        ));
    }

    @Test
    void evaluateImageSimilarity_MissionNotFound() {
        // Given
        when(missionRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            missionService.evaluateImageSimilarity(999L, 1L);
        });
        assertEquals("미션을 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    void evaluateImageSimilarity_BoardNotFound() {
        // Given
        when(missionRepository.findById(1L)).thenReturn(Optional.of(testMission));
        when(boardRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            missionService.evaluateImageSimilarity(1L, 999L);
        });
        assertEquals("게시글을 찾을 수 없습니다.", exception.getMessage());
    }
} 