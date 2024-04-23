package com.ssafy.healingdiary.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberUpdateRequest {

    private String nickname;

    private String disease;

    private String region;
}
