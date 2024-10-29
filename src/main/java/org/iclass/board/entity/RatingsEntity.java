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

    @Column(name = "USERID", nullable = false, unique = true)
    private String userId;

    @Column(name = "RATING", nullable = false, columnDefinition = "Number(2) CHECK (rating BETWEEN 10 AND 50)")
    private Integer rating; // 1-5 환산

    @CreatedDate
    @Column(name = "CREATEDAT")
    private LocalDateTime createdAt = LocalDateTime.now();
}