package com.ssafy.healingdiary.domain.member.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.healingdiary.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class LoginResponse {

    private Long id;

    private String nickname;

    private String email;
    private String region;

    private String disease;

    private String memberImageUrl;

    private String jwtToken;

    public static LoginResponse toEntity(Member member, String jwtToken) {
        return LoginResponse.builder()
                .id(member.getId())
                .email(member.getProviderEmail())
                .region(member.getRegion())
                .disease(member.getDisease())
                .nickname(member.getNickname())
                .memberImageUrl(member.getMemberImageUrl())
                .jwtToken(jwtToken)
                .build();
    }

}
