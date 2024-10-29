package org.iclass.board.repository;

import org.iclass.board.entity.CommentsEntity;
import org.iclass.board.entity.RatingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingsRepository extends JpaRepository<RatingsEntity, Long> {

    @Query("SELECT r.postId, avg(r.rating) FROM RatingsEntity r group by r.postId")
    List<Object[]> getAvgRating();


    Optional<RatingsEntity> findByPostIdAndUserId(Long postId, String userId);

    @Query("SELECT avg(r.rating) FROM RatingsEntity r GROUP BY r.postId having r.postId = :postId")
    Optional<Double> findByAvgRatingsByPostId(Long postId);
}
