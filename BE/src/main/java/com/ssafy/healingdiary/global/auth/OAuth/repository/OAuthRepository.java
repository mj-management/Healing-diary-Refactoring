package com.ssafy.healingdiary.global.auth.OAuth.repository;


import com.ssafy.healingdiary.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuthRepository extends JpaRepository<Member, String> {
    Member findById(Long id);
}

