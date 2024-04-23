package com.ssafy.healingdiary.domain.club.repository;

import com.ssafy.healingdiary.domain.club.dto.ClubInvitationResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ClubMemberRepositoryCustom {
    Slice<ClubInvitationResponse> findDistinctByClubIdNot(Long clubId, Long hostId, Pageable pageable);
}
