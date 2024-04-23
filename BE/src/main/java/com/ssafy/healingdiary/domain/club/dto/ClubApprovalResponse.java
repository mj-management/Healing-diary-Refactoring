package com.ssafy.healingdiary.domain.club.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClubApprovalResponse {

    private Long clubId;
    private Long memberId;

    public static ClubApprovalResponse of(Long clubId, Long memberId) {
        return ClubApprovalResponse.builder()
            .clubId(clubId)
            .memberId(memberId)
            .build();
    }
}
