package com.ssafy.healingdiary.global.auth;

import com.ssafy.healingdiary.domain.member.domain.Member;
import com.ssafy.healingdiary.global.auth.OAuth.repository.OAuthRepository;
import com.ssafy.healingdiary.global.error.CustomException;
import com.ssafy.healingdiary.global.jwt.JwtTokenizer;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

import static com.ssafy.healingdiary.global.error.ErrorCode.FORBIDDEN;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private static final String AUTHORITIES_KEY = "sub";

    private final OAuthRepository oAuthRepository;

    private final JwtTokenizer jwtTokenizer;


    @Override
    public PrincipalDetails loadUserByUsername(String id) throws EntityNotFoundException {
        Long Pid = Long.parseLong(id);
        Member member = oAuthRepository.findById(Pid);

        return new PrincipalDetails(member);
    }


    public Authentication getAuthentication(String token) {
        Claims claims = jwtTokenizer.parseClaims(token);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new CustomException(FORBIDDEN);
        }

        UserDetails userDetails = this.loadUserByUsername(jwtTokenizer.getId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

}
