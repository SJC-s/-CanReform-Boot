package org.iclass.board.service;

import org.iclass.board.dao.UserMapper;
import org.iclass.board.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public void registerUser(UserDTO user) {
        userMapper.save(user);
    }

    public UserDTO getUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public UserDTO login(String username, String password) {
        // 파라미터를 Map 으로 전달
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        // Mapper 메서드 호출
        return userMapper.findByUsernameAndPassword(params);
    }
}
