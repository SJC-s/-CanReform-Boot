package org.iclass.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.entity.RatingsEntity;
import org.iclass.board.repository.RatingsRepository;
import org.iclass.board.dto.RatingsDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingsService {

    private final RatingsRepository ratingsRepository;

    public RatingsDTO saveRating(RatingsDTO ratingsDTO) {
        RatingsEntity entity = ratingsDTO.toEntity();
        RatingsEntity savedEntity = ratingsRepository.save(entity);
        return RatingsDTO.of(savedEntity);
    }

    // 특정 사용자 평점 조회
    public RatingsDTO getUserRating(Long postId, String userId) {
        Optional<RatingsEntity> entity = ratingsRepository.findByPostIdAndUserId(postId, userId);

        // 조회된 결과가 없는 경우 null 반환
        if (entity.isEmpty()) {
            return null;
        }
        RatingsEntity rating = entity.get();
        return RatingsDTO.of(rating);
    }

    // 게시물의 평균 평점 조회
    public double getAverageRating(Long postId) {
        Optional<Double> avgRating = ratingsRepository.findByAvgRatingsByPostId(postId);
        if(avgRating.isEmpty()) {
            return 0;
        }
        return avgRating.get();
    }
}
