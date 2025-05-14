package com.trip.treaxure.auth.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.trip.treaxure.member.entity.Member;

import lombok.Getter;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Member member;

    public Member getMember() {
        return member;
    }

    public CustomUserDetails(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + member.getRole().name());
        // pring Security는 내부적으로 "ROLE_" 접두어가 있어야 인식
    }

    @Override public String getPassword() { return member.getPassword(); }
    @Override public String getUsername() { return member.getEmail(); }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return member.getIsActive(); }
}
