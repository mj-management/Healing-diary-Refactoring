package com.ssafy.healingdiary.domain.club.dto;

import com.ssafy.healingdiary.domain.club.domain.Club;
import com.ssafy.healingdiary.domain.club.domain.ClubMember;
import com.ssafy.healingdiary.domain.member.domain.Member;
import lombok.Getter;

@Getter
public class InvitationRegisterRequest {

    Long memberId;

    public static ClubMember toEntity(Club club, Member member) {
        return ClubMember.builder()
            .club(club)
            .member(member)
            .isApproved(false)
            .build();
    }
}
