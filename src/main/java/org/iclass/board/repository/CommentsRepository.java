package org.iclass.board.repository;

import org.iclass.board.entity.CommentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<CommentsEntity, Long> {
    List<CommentsEntity> findByPostIdOrderByCreatedAt(Long postId);
}
