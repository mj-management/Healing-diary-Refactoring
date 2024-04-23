package com.ssafy.healingdiary.global.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenRegenerateResponse {
    private String accessToken;


    public static TokenRegenerateResponse of(String accessToken) {
        return TokenRegenerateResponse.builder()
                .accessToken(accessToken)
                .build();

    }
}
