package org.iclass.board.repository;

import org.iclass.board.entity.RatingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingsRepository extends JpaRepository<RatingsEntity, Long> {
}
