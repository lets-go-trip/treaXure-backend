package com.trip.treaxure.favorite.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.trip.treaxure.board.entity.Board;
import com.trip.treaxure.board.repository.BoardRepository;
import com.trip.treaxure.favorite.dto.request.FavoriteRequestDto;
import com.trip.treaxure.favorite.dto.response.FavoriteResponseDto;
import com.trip.treaxure.favorite.entity.Favorite;
import com.trip.treaxure.favorite.repository.FavoriteRepository;
import com.trip.treaxure.member.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public List<FavoriteResponseDto> getAllFavorites() {
        return favoriteRepository.findAll().stream()
                .map(FavoriteResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public Optional<FavoriteResponseDto> getFavoriteById(Long id) {
        return favoriteRepository.findById(id)
                .map(FavoriteResponseDto::fromEntity);
    }

    public FavoriteResponseDto createFavorite(FavoriteRequestDto dto) {
        var member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        var board = boardRepository.findById(dto.getBoardId().intValue())
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        // Favorite 엔티티 생성
        var favorite = new Favorite(null, board, member, null);
        Favorite saved = favoriteRepository.save(favorite);

        // 좋아요 수 증가 및 저장
        board.setFavoriteCount(board.getFavoriteCount() + 1);
        boardRepository.save(board);

        return FavoriteResponseDto.fromEntity(saved);
    }

    public void deleteFavorite(Long id) {
        Favorite favorite = favoriteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("좋아요를 찾을 수 없습니다."));

        Board board = favorite.getBoard();

        favoriteRepository.deleteById(id);

        // 좋아요 수 감소 및 저장 (음수 방지)
        if (board.getFavoriteCount() > 0) {
            board.setFavoriteCount(board.getFavoriteCount() - 1);
            boardRepository.save(board);
        }
    }

    public int getFavoriteCountByBoardId(Integer boardId) {
        return favoriteRepository.countByBoard_BoardId(boardId);
    }
}
