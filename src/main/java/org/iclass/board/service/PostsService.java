package org.iclass.board.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.dao.PostsMapper;
import org.iclass.board.dto.PostsDTO;
import org.iclass.board.entity.PostsEntity;
import org.iclass.board.repository.PostsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PostsService {

    /* myBatis 구간 */
    private final PostsMapper postsMapper;

    public void savePost(PostsDTO post) {
        postsMapper.savePost(post);
    }


    /* JPA 구간 */
    private final PostsRepository postsRepository;

    public List<PostsDTO> list() {
        List<PostsEntity> list = postsRepository.findAll();
        return list.stream().map(PostsDTO::of)
                .collect(Collectors.toList());
    }
}
