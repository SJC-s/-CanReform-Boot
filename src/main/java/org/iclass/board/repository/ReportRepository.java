package org.iclass.board.repository;

import org.iclass.board.entity.ReportsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository

public interface ReportRepository extends JpaRepository<ReportsEntity, Long> {
    Integer countByPostId(Long postId);

    @Modifying
    @Query("UPDATE PostsEntity p SET p.reportCount = (p.reportCount + 1) WHERE p.postId = :postId")
    void updateReportsCountPlus(Long postId);
}
