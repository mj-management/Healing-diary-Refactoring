package com.ssafy.healingdiary.domain.club.repository;

import com.ssafy.healingdiary.domain.club.domain.Club;
import com.ssafy.healingdiary.domain.club.domain.ClubMember;
import com.ssafy.healingdiary.domain.club.dto.ClubInvitationResponse;
import com.ssafy.healingdiary.domain.member.domain.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {

    Slice<ClubInvitationResponse> findDistinctByClubIdNot(Long clubId, Long hostId,
        Pageable pageable);

    ClubMember findByClubAndMember(Club club, Member member);

    Slice<ClubMember> findByClubAndIsApproved(Club club, boolean isApproved, Pageable pageable);

    Slice<ClubMember> findByClubAndIsApprovedNot(Club club, boolean isApproved, Pageable pageable);
}
