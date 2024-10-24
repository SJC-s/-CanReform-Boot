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
@Table(name = "RATINGS")
public class RatingsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RATINGID")
    private Long ratingId;

    @Column(name = "POSTID", nullable = false)
    private Long postId;

    @Column(name = "USERID", nullable = false)
    private String userId;

    @Column(name = "RATING", nullable = false, columnDefinition = "CHECK (rating BETWEEN 1 AND 5)")
    private Integer rating; // 1-5

    @Lob
    @Column(name = "REVIEW")
    private String review;

    @CreatedDate
    @Column(name = "CREATEDAT")
    private LocalDateTime createdAt = LocalDateTime.now();
}