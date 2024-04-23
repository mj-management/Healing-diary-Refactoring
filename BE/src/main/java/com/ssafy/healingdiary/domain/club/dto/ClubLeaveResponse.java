package com.ssafy.healingdiary.domain.club.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClubLeaveResponse {

    Long memberId;

    public static ClubLeaveResponse of(Long memberId) {
        return ClubLeaveResponse.builder()
            .memberId(memberId)
            .build();
    }
}
