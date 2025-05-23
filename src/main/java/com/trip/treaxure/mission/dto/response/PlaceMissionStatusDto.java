package com.trip.treaxure.mission.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlaceMissionStatusDto {
    private Long placeId;
    private String placeName;
    private Integer totalMissions;
    private Integer completedMissions;
    private Boolean allCompleted;
}
