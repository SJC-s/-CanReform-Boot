package org.iclass.board.dao;

import org.apache.ibatis.annotations.Mapper;
import org.iclass.board.dto.PostDTO;

@Mapper
public interface PostMapper {
    void savePost(PostDTO post);
}
