package org.iclass.board.service;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.iclass.board.dao.UserMapper;
import org.iclass.board.dto.UserDTO;
import org.iclass.board.entity.UserEntity;
import org.iclass.board.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserMapper userMapper;


    public void registerUser(UserDTO user) {
        userMapper.save(user);
    }

    public UserDTO getUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserEntity login(String userId, String password) {
        // 이메일을 기반으로 사용자 찾기
        UserEntity user = userRepository.findByUserId(userId);

        // 사용자가 존재하고, 비밀번호가 일치하면 로그인 성공
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }

        // 로그인 실패 시 null 반환
        return null;
    }


    public UserDTO signup(UserDTO dto) {
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(encodedPassword);
        userMapper.save(dto);
        UserEntity entity = dto.toEntity();
        //userRepository.save(entity);

        return UserDTO.of(entity);
    }

    public boolean checkUsernameExists(String userId) {
        return userRepository.existsByUserId(userId);
    }

}
