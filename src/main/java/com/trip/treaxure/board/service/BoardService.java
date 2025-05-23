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

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MissionRepository missionRepository;

    public List<BoardResponseDto> getAllBoards() {
        return boardRepository.findAll().stream()
                .map(BoardResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public Optional<BoardResponseDto> getBoardById(Long id) {
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

    public void deleteBoard(Long id) {
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
}
