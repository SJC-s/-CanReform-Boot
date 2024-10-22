package org.iclass.board.dao;

import org.apache.ibatis.annotations.Mapper;
import org.iclass.board.dto.UsersDTO;

import java.util.Map;

@Mapper
public interface UserMapper {
    void save(UsersDTO user);
    UsersDTO findByUsername(String username);

    UsersDTO findByUsernameAndPassword(Map<String, Object> params);
}
