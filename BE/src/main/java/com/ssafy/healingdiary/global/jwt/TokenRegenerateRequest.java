package com.ssafy.healingdiary.global.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenRegenerateRequest {
    private String accessToken;
}
