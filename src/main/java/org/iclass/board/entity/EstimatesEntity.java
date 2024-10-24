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
@Table(name = "ESTIMATES")
public class EstimatesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ESTIMATEID")
    private Long estimateId;

    @Column(name = "POSTID", nullable = false)
    private Long postId;

    @Column(name = "USERID", nullable = false)
    private String userId;

    @Column(name = "ESTIMATEDCOST")
    private Integer estimatedCost;

    @Column(name = "FINALCOST")
    private Integer finalCost;

    @Column(name = "STATUS", nullable = false, columnDefinition = "VARCHAR2(20) CHECK (status IN ('PENDING', 'APPROVED', 'COMPLETED', 'REJECTED'))")
    private String status;

    @CreatedDate
    @Column(name = "CREATEDAT")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "UPDATEDAT")
    private LocalDateTime updatedAt;
}