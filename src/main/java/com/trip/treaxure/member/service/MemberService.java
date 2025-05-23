package com.trip.treaxure.member.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.trip.treaxure.member.dto.request.MemberRequestDto;
import com.trip.treaxure.member.dto.request.MemberUpdateRequestDto;
import com.trip.treaxure.member.dto.response.MemberResponseDto;
import com.trip.treaxure.member.entity.Member;
import com.trip.treaxure.member.repository.MemberRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public List<MemberResponseDto> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(MemberResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public Optional<MemberResponseDto> getMemberById(Long id) {
        return memberRepository.findById(id)
                .map(MemberResponseDto::fromEntity);
    }

    public MemberResponseDto createMember(MemberRequestDto dto) {
        Member member = new Member();
        member.setEmail(dto.getEmail());
        member.setPassword(dto.getPassword()); // 실전에서는 반드시 인코딩 필요
        member.setNickname(dto.getNickname());
        return MemberResponseDto.fromEntity(memberRepository.save(member));
    }

    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    // Auth 인증 후 사용

    public Member getEntityById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
    }

    public MemberResponseDto updateMember(Long memberId, MemberUpdateRequestDto dto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        if (dto.getNickname() != null) {
            member.setNickname(dto.getNickname());
        }
        if (dto.getProfileUrl() != null) {
            member.setProfileUrl(dto.getProfileUrl());
        }

        return MemberResponseDto.fromEntity(memberRepository.save(member));
    }

    @Transactional
    public void deactivateMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));

        member.setIsActive(false); // 계정 비활성화
    }

    @Transactional
    public void addPointToMember(Long memberId, int point) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 회원입니다."));
        member.setPoint(member.getPoint() + point);
        memberRepository.save(member);
    }

}
