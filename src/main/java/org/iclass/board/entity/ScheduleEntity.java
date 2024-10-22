package org.iclass.board.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "SCHEDULE")
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCHEDULEID")
    private Long scheduleId;

    @Column(name = "USERID", nullable = false)
    private String userId;

    @Column(name = "POSTID")
    private Long postId;

    @Column(name = "EVENTDATE")
    private LocalDateTime eventDate;

    @CreatedDate
    @Column(name = "CREATEDAT")
    private LocalDateTime createdAt = LocalDateTime.now();
}