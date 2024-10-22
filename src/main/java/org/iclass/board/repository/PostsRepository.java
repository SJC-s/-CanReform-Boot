package org.iclass.board.repository;

import org.iclass.board.entity.PostsEntity;
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


    // Post 검색 조건별 검색 메서드
    @Query("SELECT p FROM PostsEntity p " +
            "JOIN UserEntity u ON p.userId = u.userId " +
            "WHERE (:category = 'all' OR p.category = :category) " +
            "AND (" +
            "  (:searchClass = 'title' AND p.title LIKE %:search%) OR " +      // 제목 검색
            "  (:searchClass = 'username' AND u.username LIKE %:search%) OR " + // 작성자 검색
            "  (:searchClass = 'content' AND p.content LIKE %:search%) OR " +   // 내용 검색
            "  (:searchClass = 'all' AND (p.title LIKE %:search% OR u.username LIKE %:search% OR p.content LIKE %:search%))" + // 전체 검색
            ") " +
            "ORDER BY p.createdAt DESC")

    // @Query("SELECT p FROM PostsEntity p WHERE (:category = 'all' OR p.category = :category) AND p.title LIKE %:search% ORDER BY p.createdAt desc")
    Page<PostsEntity> findByCategoryAndTitleContainingOrderByCreatedAtDesc(Pageable pageable, String category, String search, String searchClass);

    Optional<PostsEntity> findByPostId(Long postId);
}

