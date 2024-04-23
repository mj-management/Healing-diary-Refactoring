package com.ssafy.healingdiary.domain.club.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClubInvitationResponse {
    private Long memberId;
    private String nickname;
    private String memberImageUrl;

    @QueryProjection
    public ClubInvitationResponse(Long memberId, String nickname, String memberImageUrl) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.memberImageUrl = memberImageUrl;
    }
}
