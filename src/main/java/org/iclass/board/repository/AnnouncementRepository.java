package org.iclass.board.repository;

import org.iclass.board.entity.AnnouncementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AnnouncementRepository extends JpaRepository<AnnouncementEntity, Long> {
    List<AnnouncementEntity> findTop3ByOrderByCreatedAtDesc();

    AnnouncementEntity findByAnnouncementId(Long announcementId);
}

