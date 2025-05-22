package com.trip.treaxure.visit.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.trip.treaxure.board.repository.BoardRepository;
import com.trip.treaxure.member.entity.Member;
import com.trip.treaxure.member.repository.MemberRepository;
import com.trip.treaxure.mission.dto.response.MissionResponseDto;
import com.trip.treaxure.mission.repository.MissionRepository;
import com.trip.treaxure.place.entity.Place;
import com.trip.treaxure.place.repository.PlaceRepository;
import com.trip.treaxure.visit.dto.response.VisitMissionResponseDto;
import com.trip.treaxure.visit.dto.response.VisitResponseDto;
import com.trip.treaxure.visit.entity.Visit;
import com.trip.treaxure.visit.repository.VisitRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final VisitRepository visitRepository;
    private final MemberRepository memberRepository;
    private final PlaceRepository placeRepository;
    private final MissionRepository missionRepository;
    private final BoardRepository boardRepository;

    /**
     * 장소 방문 시 호출: 새 방문 기록 생성 or 방문 횟수 증가
     */
    @Transactional
    public VisitResponseDto recordVisit(Long memberId, Long placeId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new EntityNotFoundException("장소를 찾을 수 없습니다."));

        Visit visit = visitRepository.findByMemberAndPlace(member, place)
                .map(existingVisit -> {
                    existingVisit = Visit.builder()
                            .visitId(existingVisit.getVisitId())
                            .member(existingVisit.getMember())
                            .place(existingVisit.getPlace())
                            .visitCount(existingVisit.getVisitCount() + 1)
                            .build();
                    return visitRepository.save(existingVisit);
                })
                .orElseGet(() -> visitRepository.save(
                        Visit.builder()
                                .member(member)
                                .place(place)
                                .visitCount(1)
                                .build()
                ));

        return VisitResponseDto.fromEntity(visit);
    }

    /**
     * 장소 기준 방문 기록 조회
     */
    @Transactional
    public List<VisitResponseDto> getVisitsByPlaceId(Long placeId) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new EntityNotFoundException("장소를 찾을 수 없습니다."));

        return visitRepository.findByPlace(place).stream()
                .map(VisitResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /*
     * 사용자 기준 방문 기록 조회
     */
    @Transactional
    public List<VisitResponseDto> getVisitsByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

        return visitRepository.findByMember(member).stream()
                .map(VisitResponseDto::fromEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * 사용자 방문 기록 기반으로 장소 + 미션 목록 조합 조회
     */
    @Transactional
    public List<VisitMissionResponseDto> getVisitMissionsByMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

        return visitRepository.findByMember(member).stream()
                .map((Visit visit) -> {
                    Place place = visit.getPlace();

                    // visitorCount 계산
                    long visitorCount = visitRepository.countByPlace(place);

                    // 해당 장소의 미션 리스트
                    List<MissionResponseDto> missions = missionRepository.findAllByPlace_PlaceId((long) place.getPlaceId())
                            .stream()
                            .map(MissionResponseDto::fromEntity)
                            .collect(Collectors.toList());

                    // boardCount 계산
                    int boardCount = boardRepository.countByMission_Place(place);

                    return VisitMissionResponseDto.builder()
                            .placeId((long) place.getPlaceId())
                            .placeName(place.getName())
                            .placeDescription(place.getDescription())
                            .address(place.getAddress())
                            .thumbnailUrl(place.getThumbnailUrl())
                            .missions(missions)
                            .visitorCount((int) visitorCount)
                            .boardCount(boardCount)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
