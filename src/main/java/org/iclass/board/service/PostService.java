package org.iclass.board.service;


import org.iclass.board.dao.PostMapper;
import org.iclass.board.dto.PostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    private final PostMapper postMapper;

    @Autowired
    public PostService(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    public void savePost(PostDTO post) {
        postMapper.savePost(post);
    }
}