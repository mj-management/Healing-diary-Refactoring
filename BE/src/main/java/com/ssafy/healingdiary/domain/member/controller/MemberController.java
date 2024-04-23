package com.ssafy.healingdiary.domain.member.controller;


import com.ssafy.healingdiary.domain.member.dto.MemberInfoResponse;
import com.ssafy.healingdiary.domain.member.dto.MemberUpdateResponse;
import com.ssafy.healingdiary.domain.member.dto.NicknameCheckRequest;
import com.ssafy.healingdiary.domain.member.dto.NicknameCheckResponse;
import com.ssafy.healingdiary.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    public MemberInfoResponse memberDetailInfo(Authentication authentication) {

        // 사용자 조회
        UserDetails principal = (UserDetails) authentication.getPrincipal();

        return memberService.memberInfoFind(principal.getPassword());
    }

    @PostMapping("/info")
    public MemberUpdateResponse memberInfoUpdate(Authentication authentication,
                                                 @RequestParam("nickname") String nickname,
                                                 @RequestParam("disease") String disease,
                                                 @RequestParam("region") String region,
                                                 @RequestPart(value = "image_file", required = false) MultipartFile file) throws IOException {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        return memberService.memberUpdate(principal.getPassword(), nickname, disease, region, file);
    }

    @PostMapping("/nickname")
    public NicknameCheckResponse result(@RequestBody NicknameCheckRequest memberNickname) {
        // 닉네임 조회

        return memberService.nicknameCheck(memberNickname);
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestHeader("Authorization") String tokenRegenerateRequest,
                                     HttpServletRequest request) {

        return memberService.reissue(tokenRegenerateRequest, request);
    }


}
