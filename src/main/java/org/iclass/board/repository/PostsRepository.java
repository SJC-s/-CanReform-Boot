package org.iclass.board.repository;

import org.iclass.board.entity.PostsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<PostsEntity, Long> {


    // Post 와 User 를 조인해서 작성자 정보를 포함한 게시글 목록 가져오기
    @Query("SELECT p, u.username FROM PostsEntity p JOIN UsersEntity u ON p.userId = u.userId ORDER BY p.createdAt DESC")
    List<Object[]> getPostsWithUsers();
}

