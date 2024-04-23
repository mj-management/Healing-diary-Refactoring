package com.ssafy.healingdiary.domain.member.dto;

import com.ssafy.healingdiary.domain.member.domain.Member;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class MemberInfoResponse {

    private String nickname;

    private String imageSrc;

    private String disease;

    private String region;

    public static MemberInfoResponse of(Member member) {
        return MemberInfoResponse.builder()
                .nickname(member.getNickname())
                .disease(member.getDisease())
                .imageSrc(member.getMemberImageUrl())
                .region(member.getRegion())
                .build();

    }
}
