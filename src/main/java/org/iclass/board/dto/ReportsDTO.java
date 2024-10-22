package org.iclass.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.iclass.board.entity.ReportsEntity;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportsDTO {
    private Long reportId;
    private Long postId;
    private String userId;
    private String reason;
    private LocalDateTime createdAt;

    public static ReportsDTO of(ReportsEntity entity) {
        return ReportsDTO.builder()
                .reportId(entity.getReportId())
                .postId(entity.getPostId())
                .userId(entity.getUserId())
                .reason(entity.getReason())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public ReportsEntity toEntity() {
        return ReportsEntity.builder()
                .reportId(reportId)
                .postId(postId)
                .userId(userId)
                .reason(reason)
                .createdAt(createdAt)
                .build();
    }
}