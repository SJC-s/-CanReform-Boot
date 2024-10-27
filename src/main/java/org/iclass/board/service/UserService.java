package org.iclass.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.dao.UserMapper;
import org.iclass.board.dto.UsersDTO;
import org.iclass.board.entity.UsersEntity;
import org.iclass.board.jwt.TokenProvider;
import org.iclass.board.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserMapper userMapper;


    public void registerUser(UsersDTO user) {
        userMapper.save(user);
    }

    public UsersDTO getUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    public Map<String, Object> login(UsersDTO dto) {

        // 1. UsernamePasswordAuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dto.getUserId(), dto.getPassword());

        // 2. AuthenticationManager를 통해 인증 시도
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 3. 인증이 성공하면 JWT 토큰 생성
        String jwt = tokenProvider.generateToken(authentication);

        // 4. 응답 데이터 구성
        Map<String, Object> response = new HashMap<>();
        response.put("token", jwt);
        response.put("user", authentication.getPrincipal()); // 유저 정보를 담을 수 있음

        // 아이디를 기반으로 사용자 찾기
        UsersEntity user = userRepository.findByUserId(dto.getUserId());

        // 사용자가 존재하고, 비밀번호가 일치하면 로그인 성공
        if (user != null && passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return response;
        }

        // 로그인 실패 시 null 반환
        return null;
    }


    public UsersDTO signup(UsersDTO dto) {
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(encodedPassword);
        UsersEntity entity = dto.toEntity();
        userRepository.save(entity);

        return UsersDTO.of(entity);
    }

    public boolean checkUsernameExists(String userId) {
        return userRepository.existsByUserId(userId);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
