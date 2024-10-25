package org.iclass.board.repository;

import org.apache.ibatis.annotations.Param;
import org.iclass.board.entity.PostsEntity;
import org.iclass.board.entity.ReportsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ReportRepository extends JpaRepository<ReportsEntity, Long> {
    Integer countByPostId(Long postId);

    @Modifying
    @Query("UPDATE PostsEntity p SET p.reportCount = (p.reportCount + 1) WHERE p.postId = :postId")
    void updateReportsCountPlus(Long postId);

    @Query("SELECT p FROM PostsEntity p WHERE p.reportCount > :reportCount ORDER BY p.reportCount DESC")
    List<PostsEntity> findPostsByReportCountGreaterThan(@Param("reportCount") int reportCount);

    List<ReportsEntity> findByPostId(long postId);
}
