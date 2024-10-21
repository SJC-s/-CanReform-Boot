package org.iclass.board.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.dao.PostsMapper;
import org.iclass.board.dto.PostsDTO;
import org.iclass.board.dto.UserDTO;
import org.iclass.board.entity.PostsEntity;
import org.iclass.board.entity.UserEntity;
import org.iclass.board.repository.PostsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public List<Object[]> list() {
        return postsRepository.getPostsWithUsers();
    }

    public Page<PostsDTO> getFilteredPosts(Pageable pageable, String search, String category) {
        Page<PostsEntity> postsPage = postsRepository.findByCategoryAndTitleContainingOrderByCreatedAtDesc(pageable, category, search);

        return postsPage.map(PostsDTO::of);
    }

    public PostsDTO getPostDetail(Long postId) {
        PostsEntity post = postsRepository.findByPostId(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        return PostsDTO.of(post);
    }
}
