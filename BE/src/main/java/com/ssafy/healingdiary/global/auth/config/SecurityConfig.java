package com.ssafy.healingdiary.global.auth.config;


import com.ssafy.healingdiary.global.auth.PrincipalDetailsService;
import com.ssafy.healingdiary.global.jwt.JwtAuthenticationFilter;
import com.ssafy.healingdiary.global.jwt.JwtTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenizer jwtTokenizer;
    private final PrincipalDetailsService principalDetailsService;

    @Autowired
    public SecurityConfig(JwtTokenizer jwtTokenizer, PrincipalDetailsService principalDetailsService) {
        this.jwtTokenizer = jwtTokenizer;
        this.principalDetailsService = principalDetailsService;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers(
                        "/v2/api-docs/**",
                        "/",
                        "/webjars/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html/**",
                        "/swagger-resources/**",
                        "/swagger-ui.html",
                        "/auth/account/**",
                        "/members/nickname"

                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .cors()
                .and()
                .formLogin().disable()
                .csrf().disable()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers(
                        "/",
                        "/webjars/**",
                        "/auth/account/**",
                        "/swagger-ui.html/**", "/swagger-ui/**",
                        "/v2/api-docs/**", "/swagger-resources/**",
                        "/members/nickname", "/members/reissue").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(principalDetailsService, jwtTokenizer),
                        UsernamePasswordAuthenticationFilter.class);

    }
}
