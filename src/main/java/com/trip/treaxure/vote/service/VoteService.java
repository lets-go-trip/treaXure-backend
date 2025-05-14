package com.trip.treaxure.vote.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.trip.treaxure.board.repository.BoardRepository;
import com.trip.treaxure.member.repository.MemberRepository;
import com.trip.treaxure.vote.dto.request.VoteRequestDto;
import com.trip.treaxure.vote.dto.response.VoteResponseDto;
import com.trip.treaxure.vote.entity.Vote;
import com.trip.treaxure.vote.entity.Week;
import com.trip.treaxure.vote.repository.VoteRepository;
import com.trip.treaxure.vote.repository.WeekRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final WeekRepository weekRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public List<VoteResponseDto> getAllVotes() {
        return voteRepository.findAll().stream()
                .map(VoteResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public Optional<VoteResponseDto> getVoteById(Long id) {
        return voteRepository.findById(id)
                .map(VoteResponseDto::fromEntity);
    }

    public VoteResponseDto createVote(VoteRequestDto dto) {
        Week week = weekRepository.findById(dto.getWeekId())
                .orElseThrow(() -> new EntityNotFoundException("주차 정보를 찾을 수 없습니다."));
        var member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        var board = boardRepository.findById(dto.getBoardId())
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        Vote vote = new Vote(null, week, member, board, dto.getField());

        return VoteResponseDto.fromEntity(voteRepository.save(vote));
    }

    public void deleteVote(Long id) {
        voteRepository.deleteById(id);
    }
}
