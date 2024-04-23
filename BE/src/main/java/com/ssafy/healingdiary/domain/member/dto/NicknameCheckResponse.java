package com.ssafy.healingdiary.domain.member.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NicknameCheckResponse {

    private Boolean result;

    public static NicknameCheckResponse of(Boolean result) {
        return NicknameCheckResponse.builder()
                .result(result)
                .build();
    }
}
