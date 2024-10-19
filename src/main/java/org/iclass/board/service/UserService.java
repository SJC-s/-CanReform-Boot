package org.iclass.board.service;

import lombok.RequiredArgsConstructor;
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

    public UserDTO login(String userId, String password) {
        // 파라미터를 Map 으로 전달
        Map<String, Object> params = new HashMap<>();
        params.put("username", userId);
        params.put("password", password);

        // Mapper 메서드 호출
        return userMapper.findByUsernameAndPassword(params);
    }

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

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
