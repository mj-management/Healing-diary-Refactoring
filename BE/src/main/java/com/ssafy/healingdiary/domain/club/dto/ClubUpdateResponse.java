package com.ssafy.healingdiary.domain.club.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClubUpdateResponse {
    Long clubId;

    public static ClubUpdateResponse of(Long clubId) {
        return ClubUpdateResponse.builder()
            .clubId(clubId)
            .build();
    }
}
