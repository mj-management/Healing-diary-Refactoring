package com.ssafy.healingdiary.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.healingdiary.global.auth.PrincipalDetailsService;
import com.ssafy.healingdiary.global.error.ErrorCode;
import com.ssafy.healingdiary.global.error.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final PrincipalDetailsService principalDetailsService;

    private final JwtTokenizer jwtTokenizer;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String path = request.getServletPath();
            if (path.endsWith("reissue")) {
                filterChain.doFilter(request, response);
            } else {
                String token = request.getHeader("Authorization").replace("Bearer ", "");
                boolean isTokenValid = jwtTokenizer.validateToken(token);
                if (StringUtils.hasText(token) && isTokenValid) {

                    Authentication authentication = principalDetailsService.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                }
                filterChain.doFilter(request, response);
            }
        } catch (ExpiredJwtException e) {
            response.setStatus(ErrorCode.TOKEN_NOT_VALID.getStatus().value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(new ObjectMapper().writeValueAsString(new ErrorResponse(ErrorCode.TOKEN_NOT_VALID)));
            response.getWriter().flush();
            response.getWriter().close();

        }

    }
}