package com.trip.treaxure.member.service;

import com.trip.treaxure.common.exception.ResourceNotFoundException;
import com.trip.treaxure.member.dto.BoardDetailResponse;
import com.trip.treaxure.member.dto.BoardListResponse;
import com.trip.treaxure.member.dto.MemberProfileResponse;
import com.trip.treaxure.member.dto.UpdateProfileRequest;
import com.trip.treaxure.member.entity.Member;
import com.trip.treaxure.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    public Member createMember(Member member) {
        return memberRepository.save(member);
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
    
    /**
     * 현재 로그인한 회원의 프로필 정보를 조회합니다.
     * @param userDetails 현재 로그인한 사용자 정보
     * @return 회원 프로필 응답 DTO
     */
    public MemberProfileResponse getMyProfile(UserDetails userDetails) {
        Member member = getMemberByEmail(userDetails.getUsername());
        return buildMemberProfileResponse(member);
    }
    
    /**
     * 닉네임으로 회원 프로필 정보를 조회합니다.
     * @param nickname 조회할 회원의 닉네임
     * @return 회원 프로필 응답 DTO
     */
    public MemberProfileResponse getMemberProfileByNickname(String nickname) {
        Member member = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "nickname", nickname));
        return buildMemberProfileResponse(member);
    }
    
    /**
     * 현재 로그인한 회원의 프로필 정보를 수정합니다.
     * @param userDetails 현재 로그인한 사용자 정보
     * @param request 프로필 수정 요청 DTO
     */
    @Transactional
    public void updateMyProfile(UserDetails userDetails, UpdateProfileRequest request) {
        Member member = getMemberByEmail(userDetails.getUsername());
        member.setNickname(request.getNickname());
        member.setProfileUrl(request.getProfileUrl());
        memberRepository.save(member);
    }
    
    /**
     * 현재 로그인한 회원이 작성한 게시글 목록을 조회합니다.
     * @param userDetails 현재 로그인한 사용자 정보
     * @return 게시글 목록 응답 DTO
     */
    public BoardListResponse getMyBoards(UserDetails userDetails) {
        Member member = getMemberByEmail(userDetails.getUsername());
        
        // TODO: 현재는 예시 데이터를 반환, 실제로는 DB에서 회원이 작성한 게시글을 조회하여 반환해야 합니다.
        List<BoardListResponse.PhotoDto> photos = new ArrayList<>();
        photos.add(BoardListResponse.PhotoDto.builder()
                .photoId(301)
                .missionId(101)
                .imageUrl("https://cdn.example.com/photos/301.jpg")
                .similarityScore(0.87)
                .status("SELECTED")
                .uploadedAt(LocalDateTime.now().minusDays(2))
                .build());
        photos.add(BoardListResponse.PhotoDto.builder()
                .photoId(302)
                .missionId(102)
                .imageUrl("https://cdn.example.com/photos/302.jpg")
                .similarityScore(0.92)
                .status("PENDING")
                .uploadedAt(LocalDateTime.now().minusDays(1))
                .build());
        
        return BoardListResponse.builder()
                .photos(photos)
                .build();
    }
    
    /**
     * 현재 로그인한 회원이 작성한 특정 게시글 상세 정보를 조회합니다.
     * @param userDetails 현재 로그인한 사용자 정보
     * @param boardId 조회할 게시글 ID
     * @return 게시글 상세 정보 응답 DTO
     */
    public BoardDetailResponse getMyBoardDetail(UserDetails userDetails, Integer boardId) {
        Member member = getMemberByEmail(userDetails.getUsername());
        
        // TODO: 현재는 예시 데이터를 반환, 실제로는 DB에서 회원이 작성한 특정 게시글을 조회하여 반환
        if (boardId.equals(301)) {
            return BoardDetailResponse.builder()
                    .photoId(301)
                    .missionId(101)
                    .imageUrl("https://cdn.example.com/photos/301.jpg")
                    .similarityScore(0.87)
                    .status("SELECTED")
                    .uploadedAt(LocalDateTime.now().minusDays(2))
                    .scoreAwarded(20)
                    .evaluatedAt(LocalDateTime.now().minusDays(1))
                    .feedback("우수한 사진입니다!")
                    .build();
        }
        
        throw new ResourceNotFoundException("Board", "id", boardId);
    }
    
    /**
     * 이메일로 회원 정보를 조회합니다.
     * @param email 조회할 회원의 이메일
     * @return 회원 엔티티
     */
    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Member", "email", email));
    }
    
    /**
     * 회원 엔티티를 프로필 응답 DTO로 변환합니다.
     * @param member 회원 엔티티
     * @return 회원 프로필 응답 DTO
     */
    private MemberProfileResponse buildMemberProfileResponse(Member member) {
        // TODO: 현재는 예시 데이터를 반환, 실제로는 visit 테이블을 조회하여 방문 장소 수 계산
        return MemberProfileResponse.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profileUrl(member.getProfileUrl())
                .totalScore(450) // 예시 데이터
                .visitedCount(12) // 예시 데이터
                .build();
    }
} 