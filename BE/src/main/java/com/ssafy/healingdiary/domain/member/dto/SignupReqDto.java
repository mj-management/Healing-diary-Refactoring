package com.ssafy.healingdiary.domain.member.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupReqDto {

    private String provider;
    private String nickname;
    private String region;
    private String disease;


}
