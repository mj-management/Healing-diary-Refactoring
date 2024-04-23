package com.ssafy.healingdiary.domain.club.dto;

import com.ssafy.healingdiary.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClubMemberResponse {

    private Long clubMemberId;
    private Long memberId;
    private String nickname;
    private String memberImageUrl;

    public static ClubMemberResponse of(Long clubMemberId, Member member) {
        return ClubMemberResponse.builder()
            .clubMemberId(clubMemberId)
            .memberId(member.getId())
            .nickname(member.getNickname())
            .memberImageUrl(member.getMemberImageUrl())
            .build();
    }
}
