package org.iclass.board.jwt;

import lombok.RequiredArgsConstructor;
import org.iclass.board.entity.UserEntity;
import org.iclass.board.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;

@RequiredArgsConstructor
@Service
public class SecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    // 이름으로 회원 조회
    @Override
    public CustomUserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UserEntity member = userRepository.findByUserId(userId);

        return new CustomUserDetails(member);
    }
}