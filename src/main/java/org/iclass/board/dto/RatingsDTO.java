package org.iclass.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.iclass.board.entity.RatingsEntity;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RatingsDTO {
    private Long ratingId;
    private Long postId;
    private String userId;
    private Integer rating;
    private String review;
    private LocalDateTime createdAt;

    public static RatingsDTO of(RatingsEntity entity) {
        return RatingsDTO.builder()
                .ratingId(entity.getRatingId())
                .postId(entity.getPostId())
                .userId(entity.getUserId())
                .rating(entity.getRating())
                .review(entity.getReview())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public RatingsEntity toEntity() {
        return RatingsEntity.builder()
                .ratingId(ratingId)
                .postId(postId)
                .userId(userId)
                .rating(rating)
                .review(review)
                .createdAt(createdAt)
                .build();
    }
}