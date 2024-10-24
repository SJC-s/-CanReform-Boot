package org.iclass.board.repository;

import org.iclass.board.entity.ReportsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ReportRepository extends JpaRepository<ReportsEntity, Long> {


}
