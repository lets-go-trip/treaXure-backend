package com.trip.treaxure.vote.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.trip.treaxure.vote.dto.request.WeekRequestDto;
import com.trip.treaxure.vote.dto.response.WeekResponseDto;
import com.trip.treaxure.vote.entity.Week;
import com.trip.treaxure.vote.repository.WeekRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeekService {

    private final WeekRepository weekRepository;

    public List<WeekResponseDto> getAllWeeks() {
        return weekRepository.findAll().stream()
                .map(WeekResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public Optional<WeekResponseDto> getWeekById(Long id) {
        return weekRepository.findById(id)
                .map(WeekResponseDto::fromEntity);
    }

    public WeekResponseDto createWeek(WeekRequestDto dto) {
        Week week = new Week(null, dto.getWeekStart(), dto.getWeekEnd());
        return WeekResponseDto.fromEntity(weekRepository.save(week));
    }

    public void deleteWeek(Long id) {
        weekRepository.deleteById(id);
    }
}
