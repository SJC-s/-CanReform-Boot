package org.iclass.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.iclass.board.entity.ScheduleEntity;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDTO {
    private Long scheduleId;
    private String userId;
    private Long postId;
    private LocalDateTime eventDate;
    private LocalDateTime createdAt;

    public static ScheduleDTO of(ScheduleEntity entity) {
        return ScheduleDTO.builder()
                .scheduleId(entity.getScheduleId())
                .userId(entity.getUserId())
                .postId(entity.getPostId())
                .eventDate(entity.getEventDate())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public ScheduleEntity toEntity() {
        return ScheduleEntity.builder()
                .scheduleId(scheduleId)
                .userId(userId)
                .postId(postId)
                .eventDate(eventDate)
                .createdAt(createdAt)
                .build();
    }
}