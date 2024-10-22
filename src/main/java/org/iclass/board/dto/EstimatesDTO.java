package org.iclass.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.iclass.board.entity.EstimatesEntity;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstimatesDTO {
    private Long estimateId;
    private Long postId;
    private String userId;
    private Integer estimatedCost;
    private Integer finalCost;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static EstimatesDTO of(EstimatesEntity entity) {
        return EstimatesDTO.builder()
                .estimateId(entity.getEstimateId())
                .postId(entity.getPostId())
                .userId(entity.getUserId())
                .estimatedCost(entity.getEstimatedCost())
                .finalCost(entity.getFinalCost())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public EstimatesEntity toEntity() {
        return EstimatesEntity.builder()
                .estimateId(estimateId)
                .postId(postId)
                .userId(userId)
                .estimatedCost(estimatedCost)
                .finalCost(finalCost)
                .status(status)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}