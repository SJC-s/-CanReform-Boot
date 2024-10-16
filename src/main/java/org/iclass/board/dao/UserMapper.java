package org.iclass.board.dao;

import org.apache.ibatis.annotations.Mapper;
import org.iclass.board.dto.UserDTO;

import java.util.Map;

@Mapper
public interface UserMapper {
    void save(UserDTO user);
    UserDTO findByUsername(String username);

    UserDTO findByUsernameAndPassword(Map<String, Object> params);
}
