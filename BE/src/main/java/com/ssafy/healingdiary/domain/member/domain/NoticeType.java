package com.ssafy.healingdiary.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NoticeType {
    CLUB_REGISTRATION(1),
    CLUB_INVITATION(2);
    private final int status;
}
