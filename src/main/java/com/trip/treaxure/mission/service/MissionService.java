package com.trip.treaxure.mission.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trip.treaxure.member.entity.Member;
import com.trip.treaxure.member.repository.MemberRepository;
import com.trip.treaxure.mission.dto.MissionRequest;
import com.trip.treaxure.mission.entity.Mission;
import com.trip.treaxure.mission.repository.MissionRepository;
import com.trip.treaxure.place.entity.Place;
import com.trip.treaxure.place.repository.PlaceRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * 미션 관련 비즈니스 로직 처리 서비스
 */
@Service
public class MissionService {

    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private MemberRepository memberRepository;

    /**
     * 전체 미션 목록 조회
     *
     * @return 모든 미션 목록
     */
    public List<Mission> getAllMissions() {
        return missionRepository.findAll();
    }

    /**
     * ID로 미션 조회
     *
     * @param id 미션 ID
     * @return 미션 Optional
     */
    public Optional<Mission> getMissionById(Long id) {
        return missionRepository.findById(id);
    }

    /**
     * 미션 등록
     *
     * @param mission 등록할 미션
     * @return 저장된 미션
     */
    public Mission createMissionFromDto(MissionRequest mission) {
        Place place = placeRepository.findById(mission.placeId())
            .orElseThrow(() -> new EntityNotFoundException("Place not found: " + mission.placeId()));
        Member member = memberRepository.findById(mission.memberId())
            .orElseThrow(() -> new EntityNotFoundException("Member not found: " + mission.memberId()));

        Mission m = new Mission();
        m.setPlace(place);
        m.setMember(member);
        m.setTitle(mission.title());
        m.setDescription(mission.description());
        m.setType(mission.type());
        m.setScore(mission.score());
        m.setReferenceUrl(mission.referenceUrl());
        m.setStatus(mission.status());
        m.setIsActive(mission.isActive() != null ? mission.isActive() : false);

        return missionRepository.save(m);
    }

    /**
     * 미션 정보 수정
     */
    public Optional<Mission> updateMission(Long id, Mission updated) {
        return missionRepository.findById(id)
            .map(mission -> {
                mission.setPlace(updated.getPlace());
                mission.setMember(updated.getMember());
                mission.setTitle(updated.getTitle());
                mission.setDescription(updated.getDescription());
                mission.setType(updated.getType());
                mission.setScore(updated.getScore());
                mission.setReferenceUrl(updated.getReferenceUrl());
                mission.setStatus(updated.getStatus());
                mission.setIsActive(updated.getIsActive());
                return missionRepository.save(mission);
            });
    }

    /**
     * 미션 삭제
     *
     * @param id 삭제할 미션 ID
     */
    public void deleteMission(Long id) {
        missionRepository.deleteById(id);
    }
}
