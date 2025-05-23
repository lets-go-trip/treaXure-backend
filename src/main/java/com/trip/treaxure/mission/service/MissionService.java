package com.trip.treaxure.mission.service;


import com.trip.treaxure.mission.dto.request.MissionRequestDto;
import com.trip.treaxure.mission.dto.response.MissionResponseDto;
import com.trip.treaxure.mission.entity.Mission;
import com.trip.treaxure.mission.repository.MissionRepository;
import com.trip.treaxure.member.repository.MemberRepository;
import com.trip.treaxure.place.repository.PlaceRepository;
import com.trip.treaxure.board.repository.BoardRepository;
import com.trip.treaxure.board.entity.Board;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final MemberRepository memberRepository;
    private final PlaceRepository placeRepository;
    private final BoardRepository boardRepository;

    /**
     * 전체 미션 조회
     */
    public List<MissionResponseDto> getAllMissions() {
        return missionRepository.findAll().stream()
                .map(MissionResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * ID로 미션 조회
     */
    public Optional<MissionResponseDto> getMissionById(Long id) {
        return missionRepository.findById(id)
                .map(MissionResponseDto::fromEntity);
    }

    /**
     * 미션 생성
     */
    public MissionResponseDto createMission(MissionRequestDto requestDto) {
        Mission mission = requestDto.toEntity(
                memberRepository.findById(requestDto.getMemberId())
                        .orElseThrow(() -> new EntityNotFoundException("Member not found")),
                placeRepository.findById(requestDto.getPlaceId())
                        .orElseThrow(() -> new EntityNotFoundException("Place not found"))
        );
        return MissionResponseDto.fromEntity(missionRepository.save(mission));
    }

    /**
     * 미션 삭제
     */
    public void deleteMission(Long id) {
        missionRepository.deleteById(id);
    }

    /**
     * 이미지 유사도 평가 및 점수 저장
     */
    public Float evaluateImageSimilarity(Long missionId, Long boardId) {
        Mission mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new EntityNotFoundException("미션을 찾을 수 없습니다."));
        
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));

        // Spring AI를 통한 이미지 유사도 계산
        Float similarityScore = calculateImageSimilarity(mission.getReferenceUrl(), board.getImageUrl());
        
        // 점수 저장
        board.setSimilarityScore(similarityScore);
        boardRepository.save(board);
        
        return similarityScore;
    }

    /**
     * Spring AI를 사용하여 두 이미지의 유사도를 계산
     */
    private Float calculateImageSimilarity(String referenceUrl, String targetUrl) {
        // TODO: Spring AI 구현
        // 임시로 랜덤 점수 반환
        return (float) Math.random();
    }
}