package com.ssafy.healingdiary.global.auth.OAuth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.healingdiary.domain.member.domain.Member;
import com.ssafy.healingdiary.domain.member.dto.LoginResponse;
import com.ssafy.healingdiary.domain.member.dto.SignupReqDto;
import com.ssafy.healingdiary.domain.member.repository.MemberRepository;
import com.ssafy.healingdiary.global.auth.OAuth.dto.GoogleOauthTokenResponse;
import com.ssafy.healingdiary.global.auth.OAuth.dto.KakaoOauthTokenResDto;
import com.ssafy.healingdiary.global.error.CustomException;
import com.ssafy.healingdiary.global.error.ErrorCode;
import com.ssafy.healingdiary.global.jwt.CookieUtil;
import com.ssafy.healingdiary.global.jwt.JwtTokenizer;
import com.ssafy.healingdiary.global.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class OauthService {
    private final CookieUtil cookieUtil;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final JwtTokenizer jwtTokenizer;
    private final RedisUtil redisUtil;

    private final MemberRepository memberRepository;

    public ResponseEntity<LoginResponse> googleOauthLogin(String accesstoken) throws JsonProcessingException {
        GoogleOauthTokenResponse googleOAuthResponse = this.googleOauthCheckToken(accesstoken);
        String memberEmail = "GOOGLE_" + googleOAuthResponse.getEmail();
        Member foundMember = memberRepository.findMemberByProviderEmail(memberEmail);
        if (foundMember == null) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }

        String jwtToken = jwtTokenizer.createAccessToken(foundMember.getId().toString(), foundMember.getRoleList());
        String refreshToken = jwtTokenizer.createRefreshToken(foundMember.getId().toString(), foundMember.getRoleList());

        String memberId = jwtTokenizer.getId(jwtToken);

        redisUtil.dataExpirationsInput(memberId, refreshToken, 7);
        return cookieUtil.HandlerMethod(refreshToken, LoginResponse.toEntity(foundMember, jwtToken));

    }

    public ResponseEntity<LoginResponse> kakaoOauthLogin(String accesstoken) throws JsonProcessingException {
        KakaoOauthTokenResDto kakaoOauthTokenResDto = this.kakaoOauthCheckToken(accesstoken);
        String memberEmail = "KAKAO_" + kakaoOauthTokenResDto.getKakaoOauthTokenResponse().getEmail();
        Member foundMember = memberRepository.findMemberByProviderEmail(memberEmail);
        if (foundMember == null) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }

        String jwtToken = jwtTokenizer.createAccessToken(foundMember.getId().toString(), foundMember.getRoleList());
        String refreshToken = jwtTokenizer.createRefreshToken(foundMember.getId().toString(), foundMember.getRoleList());

        String memberId = jwtTokenizer.getId(jwtToken);

        redisUtil.dataExpirationsInput(memberId, refreshToken, 7);
        return cookieUtil.HandlerMethod(refreshToken, LoginResponse.toEntity(foundMember, jwtToken));


    }

    public LoginResponse signUp(String accesstoken, SignupReqDto signupReqDto) throws JsonProcessingException {
        if (signupReqDto.getProvider().equals("GOOGLE")) {
            return this.googleSignUp(accesstoken, signupReqDto);
        } else if (signupReqDto.getProvider().equals("KAKAO")) {
            return this.kakaoSignup(accesstoken, signupReqDto);
        } else {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }
    }

    public LoginResponse googleSignUp(String accesstoken, SignupReqDto signupReqDto)
            throws JsonProcessingException {
        GoogleOauthTokenResponse googleOauthTokenResponse = this.googleOauthCheckToken(accesstoken);
        String providerEmail = "GOOGLE_" + googleOauthTokenResponse.getEmail();
        Member duplicatedMember = memberRepository.findMemberByProviderEmail(providerEmail);
        if (duplicatedMember != null) {
            throw new CustomException(ErrorCode.CONFLICT);
        }
        String userRole = "USER";
        Member newMember = Member.googleSignupMember(providerEmail,
                signupReqDto,
                googleOauthTokenResponse,
                userRole);

        Member saveUser = memberRepository.save(newMember);

        String jwtToken = jwtTokenizer.createAccessToken(saveUser.getId().toString(), saveUser.getRoleList());
        String refreshToken = jwtTokenizer.createRefreshToken(saveUser.getId().toString(), saveUser.getRoleList());

        String memberId = jwtTokenizer.getId(jwtToken);

        redisUtil.dataExpirationsInput(memberId, refreshToken, 7);

        return LoginResponse.toEntity(saveUser, jwtToken);
    }

    public LoginResponse kakaoSignup(String accesstoken, SignupReqDto signupReqDto)
            throws JsonProcessingException {
        KakaoOauthTokenResDto kakaoOauthTokenResDto = this.kakaoOauthCheckToken(accesstoken);
        String providerEmail = "KAKAO_" + kakaoOauthTokenResDto.getKakaoOauthTokenResponse().getEmail();

        Member duplicatedMember = memberRepository.findMemberByProviderEmail(providerEmail);
        if (duplicatedMember != null) {
            throw new CustomException(ErrorCode.CONFLICT);
        }
        String userRole = "USER";

        Member newMember = Member.kakaoSignupMember(providerEmail, signupReqDto, kakaoOauthTokenResDto, userRole);
        Member saveUser = memberRepository.save(newMember);

        String jwtToken = jwtTokenizer.createAccessToken(saveUser.getId().toString(), saveUser.getRoleList());
        String refreshToken = jwtTokenizer.createRefreshToken(saveUser.getId().toString(), saveUser.getRoleList());

        String memberId = jwtTokenizer.getId(jwtToken);

        redisUtil.dataExpirationsInput(memberId, refreshToken, 7);

        return LoginResponse.toEntity(saveUser, jwtToken);
    }

    public GoogleOauthTokenResponse googleOauthCheckToken(String accesstoken)
            throws JsonProcessingException {
        String GOOGLE_USERINFO_REQUEST_URL = "https://www.googleapis.com/userinfo/v2/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accesstoken);
        HttpEntity<GoogleOauthTokenResponse> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                GOOGLE_USERINFO_REQUEST_URL,
                HttpMethod.GET,
                request,
                String.class
        );

        return objectMapper.readValue(response.getBody(), GoogleOauthTokenResponse.class);
    }

    public KakaoOauthTokenResDto kakaoOauthCheckToken(String accessToken)
            throws JsonProcessingException {
        String KAKAO_USERINFO_REQUEST_URL = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);
        HttpEntity<GoogleOauthTokenResponse> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                KAKAO_USERINFO_REQUEST_URL,
                HttpMethod.GET,
                request,
                String.class
        );

        return objectMapper.readValue(response.getBody(), KakaoOauthTokenResDto.class);
    }

}
