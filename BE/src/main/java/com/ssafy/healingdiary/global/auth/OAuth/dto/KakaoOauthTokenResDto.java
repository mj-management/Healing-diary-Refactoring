package com.ssafy.healingdiary.global.auth.OAuth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KakaoOauthTokenResDto {
    @JsonProperty("properties")
    private KakaoOauthTokenResProperties kakaoOauthTokenResProperties;
    @JsonProperty("kakao_account")
    private KakaoOauthTokenResponse kakaoOauthTokenResponse;


}
