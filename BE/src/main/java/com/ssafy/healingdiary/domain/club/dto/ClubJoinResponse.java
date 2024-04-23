package com.ssafy.healingdiary.domain.club.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClubJoinResponse {

    private Long clubId;
    private String msg;

    public static ClubJoinResponse of(Long clubId) {
        return ClubJoinResponse.builder()
            .clubId(clubId)
            .build();
    }

    public static ClubJoinResponse of(String msg) {
        return ClubJoinResponse.builder()
            .msg(msg)
            .build();
    }
}
