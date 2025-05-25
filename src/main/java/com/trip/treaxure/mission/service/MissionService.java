package com.trip.treaxure.mission.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.trip.treaxure.board.entity.Board;
import com.trip.treaxure.board.repository.BoardRepository;
import com.trip.treaxure.member.repository.MemberRepository;
import com.trip.treaxure.mission.dto.request.MissionRequestDto;
import com.trip.treaxure.mission.dto.response.MissionResponseDto;
import com.trip.treaxure.mission.dto.response.PlaceMissionStatusDto;
import com.trip.treaxure.mission.entity.Mission;
import com.trip.treaxure.mission.repository.MissionRepository;
import com.trip.treaxure.place.entity.Place;
import com.trip.treaxure.place.repository.PlaceRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

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
     * 장소 ID로 미션 조회
     */
    public List<MissionResponseDto> getMissionsByPlaceId(Long placeId) {
        return missionRepository.findAllByPlace_PlaceId(placeId).stream()
                .map(MissionResponseDto::fromEntity)
                .collect(Collectors.toList());
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
     * 사용자별 장소별 미션 완료 현황 반환 (N+1 방지 최적화)
     */
    public List<PlaceMissionStatusDto> getMissionCompletionStatusByMember(Long memberId) {
        // 1. 장소 전체
        List<Place> places = placeRepository.findAll();

        // 2. 모든 미션을 장소 포함해서 조회
        List<Mission> allMissions = missionRepository.findAllWithPlace();

        // 3. 사용자의 모든 board + mission 조회
        List<Board> userBoards = boardRepository.findAllByMemberIdWithMission(memberId);
        Set<Long> completedMissionIds = userBoards.stream()
                .map(b -> b.getMission().getMissionId())
                .collect(Collectors.toSet());

        // 4. 장소별 미션 그룹핑
        Map<Long, List<Mission>> placeIdToMissions = allMissions.stream()
                .collect(Collectors.groupingBy(m -> m.getPlace().getPlaceId().longValue()));

        // 5. 집계
        List<PlaceMissionStatusDto> result = new ArrayList<>();
        for (Place place : places) {
            List<Mission> missions = placeIdToMissions.getOrDefault(place.getPlaceId().longValue(), List.of());
            int total = missions.size();
            int completed = (int) missions.stream()
                    .filter(m -> completedMissionIds.contains(m.getMissionId()))
                    .count();

            result.add(new PlaceMissionStatusDto(
                    place.getPlaceId().longValue(),
                    place.getName(),
                    total,
                    completed,
                    total > 0 && total == completed
            ));
        }

        return result;
    }
}
