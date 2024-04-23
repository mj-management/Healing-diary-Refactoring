package com.ssafy.healingdiary.global.auth.OAuth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KakaoOauthTokenResProperties {
    @JsonProperty("profile_image")
    private String profileImage;
}
