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
@Table(name = "COMMENTS")
public class CommentsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENTID")
    private Long commentId;

    @Column(name = "POSTID", nullable = false)
    private Long postId;

    @Column(name = "USERID", nullable = false)
    private String userId;

    @Lob
    @Column(name = "CONTENT")
    private String content;

    @CreatedDate
    @Column(name = "CREATEDAT", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}