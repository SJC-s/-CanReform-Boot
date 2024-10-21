package org.iclass.board.jwt;

import lombok.AllArgsConstructor;
import org.iclass.board.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private UserEntity userEntity;


    // 권환 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<String> roles = Collections.singletonList(userEntity.getUsersRole());

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    // 비밀번호 반환
    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    // 이름 반환
    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    // 계정 만료 여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠김 여부
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 자격 증명 만료 여부
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화 여부
    @Override
    public boolean isEnabled() {
        return true;
    }

}