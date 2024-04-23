package com.ssafy.healingdiary.domain.club.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InvitationRegisterResponse {

    private Long clubId;
    private String msg;

    public static InvitationRegisterResponse of(Long clubId) {
        return InvitationRegisterResponse.builder()
            .clubId(clubId)
            .build();
    }

    public static InvitationRegisterResponse of(String msg) {
        return InvitationRegisterResponse.builder()
            .msg(msg)
            .build();
    }
}
