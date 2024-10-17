package org.iclass.board.dao;

import org.apache.ibatis.annotations.Mapper;
import org.iclass.board.dto.PostsDTO;

@Mapper
public interface PostsMapper {
    void savePost(PostsDTO post);
}
