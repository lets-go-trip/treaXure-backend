package com.trip.treaxure.favorite.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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

        var favorite = new Favorite(null, board, member, null); // createdAt 자동

        return FavoriteResponseDto.fromEntity(favoriteRepository.save(favorite));
    }

    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
}
