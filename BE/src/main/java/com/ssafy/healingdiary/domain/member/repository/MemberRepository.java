package com.ssafy.healingdiary.domain.member.repository;

import com.ssafy.healingdiary.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findMemberByProviderEmail(String providerEmail);

    Member findMemberByNickname(String nickname);
}
