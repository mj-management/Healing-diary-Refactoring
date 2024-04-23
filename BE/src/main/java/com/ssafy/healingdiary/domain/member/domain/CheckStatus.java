package com.ssafy.healingdiary.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CheckStatus {
    UNCHECKED(false), CHECKED(true);
    private final boolean value;

    public static CheckStatus ofValue(boolean value) {
        return value ? CHECKED : UNCHECKED;
    }

    public static boolean ofFlag(CheckStatus flag) {
        return CheckStatus.CHECKED == flag ? true : false;
    }
}
