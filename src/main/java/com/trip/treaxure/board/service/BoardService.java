package com.trip.treaxure.board.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.trip.treaxure.board.dto.request.BoardRequestDto;
import com.trip.treaxure.board.dto.response.BoardResponseDto;
import com.trip.treaxure.board.entity.Board;
import com.trip.treaxure.board.repository.BoardRepository;
import com.trip.treaxure.favorite.repository.FavoriteRepository;
import com.trip.treaxure.global.service.ImageSimilarityService;
import com.trip.treaxure.global.service.LocalImageSimilarityService;
import com.trip.treaxure.global.service.OpenAiVisionSimilarityService;
import com.trip.treaxure.mission.entity.Mission;
import com.trip.treaxure.mission.repository.MissionRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MissionRepository missionRepository;
    private final FavoriteRepository favoriteRepository;

    private final ImageSimilarityService imageSimilarityService;
    private final LocalImageSimilarityService localImageSimilarityService;
    private final OpenAiVisionSimilarityService openAiVisionSimilarityService;

    public List<BoardResponseDto> getAllBoardsWithDetails() {
        return boardRepository.findAllWithMissionPlaceAndMember().stream()
                .map(BoardResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public Optional<BoardResponseDto> getBoardById(Integer id) {
        return boardRepository.findById(id)
                .map(BoardResponseDto::fromEntity);
    }

    public BoardResponseDto createBoard(BoardRequestDto dto) {
        Mission mission = missionRepository.findById(dto.getMissionId())
                .orElseThrow(() -> new EntityNotFoundException("미션을 찾을 수 없습니다."));

        Board board = Board.builder()
                .memberId(dto.getMemberId())
                .mission(mission)
                .imageUrl(dto.getImageUrl())
                .isActive(true)
                .title(dto.getTitle())
                .build();

        return BoardResponseDto.fromEntity(boardRepository.save(board));
    }

    public void deleteBoard(Integer id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시물을 찾을 수 없습니다."));
        board.setIsActive(false);
        boardRepository.save(board);
    }

    public Optional<Board> getBoardByMissionAndMember(Long missionId, Long memberId) {
        return boardRepository.findByMission_MissionIdAndMemberId(missionId, memberId);
    }

    public List<BoardResponseDto> getBoardsByMemberWithFavorites(Long memberId) {
        return boardRepository.findAllByMemberId(memberId).stream()
                .map(this::convertToDtoWithFavoriteCount)
                .collect(Collectors.toList());
    }

    /**
     * 이미지 유사도 평가 및 점수 저장
     * 
     * @param useOpenAI OpenAI 서비스 사용 여부 (true: OpenAI, false: 로컬)
     */
    public Float evaluateImageSimilarity(Long missionId, Integer boardId, Boolean useOpenAI) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new EntityNotFoundException("미션을 찾을 수 없습니다."));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        // 선택한 서비스에 따라 이미지 유사도 계산
        Float similarityScore;
        if (Boolean.TRUE.equals(useOpenAI)) {
            similarityScore = openAiVisionSimilarityService.compare(mission.getReferenceUrl(), board.getImageUrl());
        } else {
            similarityScore = localImageSimilarityService.compare(mission.getReferenceUrl(), board.getImageUrl());
        }

        // 점수 저장
        board.setSimilarityScore(similarityScore);
        boardRepository.save(board);

        return similarityScore;
    }

    /**
     * 기존 메소드와의 호환성을 위한 오버로딩 메소드 (기본값: 로컬 서비스 사용)
     */
    public Float evaluateImageSimilarity(Long missionId, Integer boardId) {
        return evaluateImageSimilarity(missionId, boardId, false);
    }

    private BoardResponseDto convertToDtoWithFavoriteCount(Board board) {
        int favoriteCount = favoriteRepository.countByBoard_BoardId(board.getBoardId());
        board.setFavoriteCount(favoriteCount);
        return BoardResponseDto.fromEntity(board);
    }

    public List<BoardResponseDto> getTopRankedBoards(int days) {
        Pageable limitTen = PageRequest.of(0, 10);
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);

        return boardRepository.findTopRankedBoards(startDate, limitTen).stream()
                .map(BoardResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

}
