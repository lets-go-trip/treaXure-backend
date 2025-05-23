package com.trip.treaxure.board.service;

import com.trip.treaxure.board.entity.Board;
import com.trip.treaxure.board.repository.BoardRepository;
import com.trip.treaxure.global.service.ImageSimilarityService;
import com.trip.treaxure.mission.entity.Mission;
import com.trip.treaxure.mission.repository.MissionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private MissionRepository missionRepository;

    @Mock
    private ImageSimilarityService imageSimilarityService;

    @InjectMocks
    private BoardService boardService;

    private Mission mission;
    private Board board;
    private final Long missionId = 1L;
    private final Integer boardId = 1;
    private final String referenceUrl = "https://example.com/reference.jpg";
    private final String imageUrl = "https://example.com/image.jpg";
    private final float similarityScore = 0.85f;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 설정
        mission = Mission.builder()
                .missionId(missionId)
                .referenceUrl(referenceUrl)
                .title("테스트 미션")
                .description("테스트 설명")
                .type(Mission.MissionType.PHOTO)
                .score(100)
                .status(Mission.MissionStatus.APPROVED)
                .createdAt(LocalDateTime.now())
                .evaluatedAt(LocalDateTime.now())
                .isActive(true)
                .build();

        board = new Board();
        board.setBoardId(boardId);
        board.setImageUrl(imageUrl);
        board.setMission(mission);
    }

    @Test
    void evaluateImageSimilarity_Success() {
        // Given
        when(missionRepository.findById(missionId)).thenReturn(Optional.of(mission));
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        when(imageSimilarityService.compare(referenceUrl, imageUrl)).thenReturn(similarityScore);

        // When
        Float result = boardService.evaluateImageSimilarity(missionId, boardId);

        // Then
        assertEquals(similarityScore, result);
        assertEquals(similarityScore, board.getSimilarityScore());
        verify(boardRepository).save(board);
    }

    @Test
    void evaluateImageSimilarity_MissionNotFound() {
        // Given
        when(missionRepository.findById(missionId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            boardService.evaluateImageSimilarity(missionId, boardId);
        });
        
        verify(boardRepository, never()).save(any());
    }

    @Test
    void evaluateImageSimilarity_BoardNotFound() {
        // Given
        when(missionRepository.findById(missionId)).thenReturn(Optional.of(mission));
        when(boardRepository.findById(boardId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            boardService.evaluateImageSimilarity(missionId, boardId);
        });
        
        verify(boardRepository, never()).save(any());
    }
} 