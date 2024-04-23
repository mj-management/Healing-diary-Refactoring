package com.ssafy.healingdiary.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeleteNoticeRequest {

    Long noticeId;
}
