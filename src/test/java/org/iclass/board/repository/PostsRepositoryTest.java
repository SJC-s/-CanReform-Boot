package org.iclass.board.repository;

import lombok.extern.slf4j.Slf4j;
import org.iclass.board.entity.PostsEntity;
import org.iclass.board.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Slf4j
class PostsRepositoryTest {
    @Autowired
    private PostsRepository postsRepository;

    @Test
    void findAll() {
        List<PostsEntity> optional = postsRepository.findAll();

        optional.forEach(e -> {
            log.info("entity: {}", e);
        });
        assertNotNull(optional);
    }
}
