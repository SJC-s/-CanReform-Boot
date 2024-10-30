package org.iclass.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.iclass.board.entity.CommentsEntity;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentsDTO {
    private Long commentId;
    private Long postId;
    private String userId;
    private String content;
    private LocalDateTime createdAt;
    private Long announcementId;

    public static CommentsDTO of(CommentsEntity entity) {
        return CommentsDTO.builder()
                .commentId(entity.getCommentId())
                .postId(entity.getPostId())
                .userId(entity.getUserId())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .announcementId(entity.getAnnouncementId())
                .build();
    }

    public CommentsEntity toEntity() {
        return CommentsEntity.builder()
                .commentId(commentId)
                .postId(postId)
                .userId(userId)
                .content(content)
                .createdAt(createdAt)
                .announcementId(announcementId)
                .build();
    }
}