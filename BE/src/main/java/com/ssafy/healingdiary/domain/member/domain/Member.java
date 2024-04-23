package com.ssafy.healingdiary.domain.member.domain;

import com.ssafy.healingdiary.domain.club.domain.ClubMember;
import com.ssafy.healingdiary.domain.diary.domain.Diary;
import com.ssafy.healingdiary.domain.member.dto.SignupReqDto;
import com.ssafy.healingdiary.global.auth.OAuth.dto.GoogleOauthTokenResponse;
import com.ssafy.healingdiary.global.auth.OAuth.dto.KakaoOauthTokenResDto;
import com.ssafy.healingdiary.global.common.domain.BaseEntity;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
@AttributeOverride(name = "id", column = @Column(name = "member_id"))
@AttributeOverride(name = "createdDate", column = @Column(name = "member_created_date"))
@AttributeOverride(name = "updatedDate", column = @Column(name = "member_updated_date"))
public class Member extends BaseEntity {


    @NotNull
    @Column(name = "provider_email")
    private String providerEmail;

    private String nickname;

    private String region;

    private String disease;

    @Column(name = "member_image_url")
    private String memberImageUrl;

    private String roles; // USER, MANAGER, ADMIN

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Diary> diary = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<ClubMember> clubMember = new ArrayList<>();

    public List<String> getRoleList() {
        if (this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }

    public void updateMember(String nickname, String disease,
                             String region, String imageUrl) {
        this.nickname = nickname;
        this.region = region;
        this.disease = disease;
        this.memberImageUrl = imageUrl;
    }

    public static Member googleSignupMember(String providerEmail,
                                            SignupReqDto signupReqDto,
                                            GoogleOauthTokenResponse googleOauthTokenResponse,
                                            String userRole) {
        return Member.builder()
                .providerEmail(providerEmail)
                .nickname(signupReqDto.getNickname())
                .region(signupReqDto.getRegion())
                .disease(signupReqDto.getDisease())
                .memberImageUrl(googleOauthTokenResponse.getPicture())
                .roles(userRole)
                .build();
    }

    public static Member kakaoSignupMember(String providerEmail,
                                           SignupReqDto signupReqDto,
                                           KakaoOauthTokenResDto kakaoOauthTokenResDto,
                                           String userRole) {
        return Member.builder()
                .providerEmail(providerEmail)
                .nickname(signupReqDto.getNickname())
                .region(signupReqDto.getRegion())
                .disease(signupReqDto.getDisease())
                .memberImageUrl(kakaoOauthTokenResDto.getKakaoOauthTokenResProperties().getProfileImage())
                .roles(userRole)
                .build();
    }


}