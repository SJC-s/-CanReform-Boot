package org.iclass.board.repository;

import org.iclass.board.entity.PostsEntity;
import org.iclass.board.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostsRepository extends JpaRepository<PostsEntity, Long> {


    // Post와 User를 조인해서 작성자 정보를 포함한 게시글 목록 가져오기
    @Query("SELECT p, u.username FROM PostsEntity p JOIN UserEntity u ON p.userId = u.userId ORDER BY p.createdAt DESC")
    List<Object[]> getPostsWithUsers();

    @Query("SELECT p FROM PostsEntity p WHERE (:category = 'all' OR p.category = :category) AND p.title LIKE %:search% ORDER BY p.createdAt desc")
    Page<PostsEntity> findByCategoryAndTitleContainingOrderByCreatedAtDesc(Pageable pageable, String category, String search);

    @Query("SELECT p FROM PostsEntity p WHERE (:category = 'all' OR p.category = :category) AND p.content LIKE %:search% ORDER BY p.createdAt desc")
    Page<PostsEntity> findByCategoryAndContentContainingOrderByCreatedAtDesc(Pageable pageable, String category, String search);

    @Query("SELECT p FROM PostsEntity p WHERE (:category = 'all' OR p.category = :category) AND p.userId LIKE %:search% ORDER BY p.createdAt desc")
    Page<PostsEntity> findByCategoryAndUserIdContainingOrderByCreatedAtDesc(Pageable pageable, String category, String search);

    @Query("SELECT p FROM PostsEntity p WHERE (:category = 'all' OR p.category = :category) " +
            "AND (p.title LIKE %:search% OR p.userId LIKE %:search% OR p.content LIKE %:search%) ORDER BY p.createdAt desc")
    Page<PostsEntity> findByCategoryAndTitleOrUserIdOrContentContainingOrderByCreatedAtDesc(Pageable pageable, String category, String search);


    Optional<PostsEntity> findByPostId(Long postId);
}

