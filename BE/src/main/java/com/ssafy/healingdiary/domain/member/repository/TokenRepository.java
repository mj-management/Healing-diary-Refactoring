package com.ssafy.healingdiary.domain.member.repository;

import com.ssafy.healingdiary.domain.member.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Long> {

    @Query("select t " +
            "from Token t " +
            "join fetch t.member m " +
            "where t.refreshToken =:refreshToken")
    Optional<Token> findToken(String refreshToken);


}
