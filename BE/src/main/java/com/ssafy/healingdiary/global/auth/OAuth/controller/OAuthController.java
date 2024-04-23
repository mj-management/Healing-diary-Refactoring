package com.ssafy.healingdiary.global.auth.OAuth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.healingdiary.domain.member.dto.LoginResponse;
import com.ssafy.healingdiary.domain.member.dto.SignupReqDto;
import com.ssafy.healingdiary.global.auth.OAuth.service.OauthService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/auth/account")
@Slf4j
public class OAuthController {

    private final OauthService oauthService;

    @ApiOperation(value = "구글 로그인", notes = "구글 엑세스 토큰을 이용하여 사용자 정보 받아 저장하고 앱의 토큰 반환해줌")
    @GetMapping(value = "/google/login")
    public ResponseEntity<LoginResponse> googleAuthRequest(@RequestHeader("Authorization") String accessToken) {

        try {
            ResponseEntity<LoginResponse> loginResponse = oauthService.googleOauthLogin(accessToken);
            return loginResponse;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @ApiOperation(value = "카카오 로그인", notes = "카카오 엑세스 토큰을 이용해서 사용자 정보 받아 저장하고 앱의 토큰 반환해줌")
    @GetMapping(value = "/kakao/login")
    public ResponseEntity<LoginResponse> kakaoAuthRequest(@RequestHeader("Authorization") String accessToken) {

        try {
            ResponseEntity<LoginResponse> loginResponse = oauthService.kakaoOauthLogin(accessToken);
            return loginResponse;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;

    }

    @ApiOperation(value = "회원가입", notes = "회원가입, 어떤 소셜 회원가입인지 넣어주는 것이 중요함")
    @PostMapping("/signup")
    public ResponseEntity<LoginResponse> googleSignUp(@RequestHeader("Authorization") String accessToken,
                                                      @RequestBody SignupReqDto signupReqDto) {
        try {
            LoginResponse loginResponse = oauthService.signUp(accessToken, signupReqDto);
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}

