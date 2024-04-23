package com.ssafy.healingdiary.domain.club.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClubApprovalRequest {

    private Long clubMemberId;
    private Long clubId;
    private Long memberId;

    public static ClubApprovalRequest toEntity(Long clubMemberId, Long clubId, Long memberId) {
        return ClubApprovalRequest.builder()
            .clubMemberId(clubMemberId)
            .clubId(clubId)
            .memberId(memberId)
            .build();
    }
}
