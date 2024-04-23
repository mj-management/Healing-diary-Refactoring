package com.ssafy.healingdiary.domain.club.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClubRegisterResponse {
    Long clubId;

    public static ClubRegisterResponse of(Long clubId) {
        return ClubRegisterResponse.builder()
            .clubId(clubId)
            .build();
    }
}
