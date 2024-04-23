package com.ssafy.healingdiary.domain.club.repository;

import com.ssafy.healingdiary.domain.club.dto.ClubListResponse;
import com.ssafy.healingdiary.domain.club.dto.ClubSimpleResponse;
import com.ssafy.healingdiary.domain.member.domain.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ClubRepositoryCustom {

    Slice<ClubSimpleResponse> findByOption(Boolean all, Long memberId, String keyword, String tagContent, Pageable pageable);

    Slice<ClubListResponse> findUnionList(Member member, Pageable pageable);
}
