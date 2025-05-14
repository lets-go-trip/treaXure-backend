package com.trip.treaxure.auth.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.trip.treaxure.member.entity.Member;

public class CustomUserDetails implements UserDetails {
    private final Member member;

    public CustomUserDetails(Member member) {
        this.member = member;
    }

    public Member getMember() {
        return this.member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + member.getRole().name()));
    }

    @Override
    public String getPassword() { return member.getPassword(); }

    @Override
    public String getUsername() { return member.getEmail(); }

    @Override public boolean isAccountNonExpired()   { return member.getIsActive(); }
    @Override public boolean isAccountNonLocked()    { return member.getIsActive(); }
    @Override public boolean isCredentialsNonExpired(){ return member.getIsActive(); }
    @Override public boolean isEnabled()             { return member.getIsActive(); }
}
