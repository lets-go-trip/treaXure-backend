package com.trip.treaxure.mission.service;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.trip.treaxure.member.repository.MemberRepository;
import com.trip.treaxure.mission.dto.request.MissionRequestDto;
import com.trip.treaxure.mission.dto.response.MissionResponseDto;
import com.trip.treaxure.mission.entity.Mission;
import com.trip.treaxure.mission.repository.MissionRepository;
import com.trip.treaxure.place.repository.PlaceRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final MemberRepository memberRepository;
    private final PlaceRepository placeRepository;

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
}