package org.iclass.board.repository;

import org.iclass.board.entity.PostsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostsRepository extends JpaRepository<PostsEntity, Long> {

    @Query(value = "SELECT p.* " +
            "FROM posts p " +
            "LEFT JOIN (SELECT postId, AVG(rating) as avgRating FROM ratings GROUP BY postId) r " +
            "ON p.postId = r.postId " +
            "WHERE p.ISPRIVATE = 'Y' AND p.CATEGORY = 'request' " +
            "ORDER BY r.avgRating DESC, p.createdAt DESC",
            nativeQuery = true)
    List<PostsEntity> getBoardToMain();


    // Post 와 User 를 조인해서 작성자 정보를 포함한 게시글 목록 가져오기
    @Query("SELECT p, u.username FROM PostsEntity p JOIN UsersEntity u ON p.userId = u.userId ORDER BY p.createdAt DESC")
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

    @Modifying
    @Query("UPDATE PostsEntity p SET p.readCount = (p.readCount + 1) WHERE p.postId = :postId")
    void updateReadCountPlus(Long postId);

    @Modifying
    @Query("UPDATE PostsEntity p SET p.commentCount = (p.commentCount + 1) WHERE p.postId = :postId")
    void updateCommentCountPlus(Long postId);

    @Modifying
    @Query("UPDATE PostsEntity p SET p.commentCount = (p.commentCount - 1) WHERE p.postId = (SELECT c.postId FROM CommentsEntity c WHERE :commentId = c.commentId)")
    void updateCommentCountMinus(Long commentId);
}

