package org.iclass.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.dao.UserMapper;
import org.iclass.board.dto.UsersDTO;
import org.iclass.board.entity.UsersEntity;
import org.iclass.board.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UsersEntity login(String userId, String password) {
        // 아이디를 기반으로 사용자 찾기
        UsersEntity user = userRepository.findByUserId(userId);

        // 사용자가 존재하고, 비밀번호가 일치하면 로그인 성공
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
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
