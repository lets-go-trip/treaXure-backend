package com.trip.treaxure.board.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.trip.treaxure.board.dto.request.BoardRequestDto;
import com.trip.treaxure.board.dto.response.BoardResponseDto;
import com.trip.treaxure.board.entity.Board;
import com.trip.treaxure.board.repository.BoardRepository;
import com.trip.treaxure.mission.entity.Mission;
import com.trip.treaxure.mission.repository.MissionRepository;
import com.trip.treaxure.global.service.ImageSimilarityService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MissionRepository missionRepository;
    private final ImageSimilarityService imageSimilarityService;

    public List<BoardResponseDto> getAllBoards() {
        return boardRepository.findAll().stream()
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
                .favoriteCount(0)
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

    public List<Board> getBoardsByMember(Long memberId) {
        return boardRepository.findAllByMemberId(memberId);
    }
    
    /**
     * 이미지 유사도 평가 및 점수 저장
     */
    public Float evaluateImageSimilarity(Long missionId, Integer boardId) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new EntityNotFoundException("미션을 찾을 수 없습니다."));
        
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        // 이미지 유사도 계산
        Float similarityScore = imageSimilarityService.compare(mission.getReferenceUrl(), board.getImageUrl());
        
        // 점수 저장
        board.setSimilarityScore(similarityScore);
        boardRepository.save(board);
        
        return similarityScore;
    }
}
