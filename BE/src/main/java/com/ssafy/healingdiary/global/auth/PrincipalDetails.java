package com.ssafy.healingdiary.global.auth;

import com.ssafy.healingdiary.domain.member.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class PrincipalDetails implements UserDetails {

    private final Member member;

    public PrincipalDetails(Member member) {
        this.member = member;
    }

    public Long getId() {
        return member.getId();
    }

    @Override
    public String getUsername() {
        return member.getId().toString();
    }

    @Override
    public String getPassword() {
        return member.getProviderEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return member.getRoleList().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
