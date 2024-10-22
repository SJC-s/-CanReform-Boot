package org.iclass.board.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.dao.PostsMapper;
import org.iclass.board.dto.PostsDTO;
import org.iclass.board.entity.PostsEntity;
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

    public Page<PostsDTO> getFilteredPosts(Pageable pageable, String search, String category, String searchClass) {
        Page<PostsEntity> postsPage;

        // 전체 검색 (title, content, userId)
        if ("all".equals(searchClass)) {
            postsPage = postsRepository.findByCategoryAndTitleOrUserIdOrContentContainingOrderByCreatedAtDesc(pageable, category, search);
        }
        // 제목으로 검색
        else if ("title".equals(searchClass)) {
            postsPage = postsRepository.findByCategoryAndTitleContainingOrderByCreatedAtDesc(pageable, category, search);
        }
        // 내용으로 검색
        else if ("content".equals(searchClass)) {
            postsPage = postsRepository.findByCategoryAndContentContainingOrderByCreatedAtDesc(pageable, category, search);
        }
        // 작성자로 검색
        else if ("userId".equals(searchClass)) {
            postsPage = postsRepository.findByCategoryAndUserIdContainingOrderByCreatedAtDesc(pageable, category, search);
        }
        // 예외 처리: 잘못된 searchClass 값이 들어온 경우
        else {
            throw new IllegalArgumentException("Invalid searchClass: " + searchClass);
        }

        return postsPage.map(PostsDTO::of);
    }

    public PostsDTO getPostDetail(Long postId) {
        PostsEntity post = postsRepository.findByPostId(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        return PostsDTO.of(post);
    }

    public PostsDTO createPost(PostsDTO postsDTO) {
        //postsMapper.savePost(postsDTO);
        PostsEntity post = postsDTO.toEntity();
        postsRepository.save(post);
        return PostsDTO.of(post);
    }
}
