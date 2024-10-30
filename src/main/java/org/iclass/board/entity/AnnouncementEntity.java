package org.iclass.board.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ANNOUNCEMENT")

public class AnnouncementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ANNOUNCEMENTID")
    private Long announcementId;

    @Column(name = "USERID", nullable = false)
    private String userId;

    @Column(name = "TITLE", nullable = false, length = 100)
    private String title;

    @Lob
    @Column(name = "CONTENT")
    private String content;

    @Column(name = "CATEGORY", nullable = false, length = 50, columnDefinition = "VARCHAR2(50) DEFAULT 'ANNOUNCEMENT'")
    private String category;

    @Column(name = "FILENAMES")
    private String filenames;

    @Column(name = "READCOUNT", columnDefinition = "NUMBER(10) DEFAULT 0")
    private Integer readCount = 0;

    @Column(name = "COMMENTCOUNT", columnDefinition = "NUMBER(10) DEFAULT 0")
    private Integer commentCount = 0;

    @Column(name = "CREATEDAT")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "UPDATEDAT")
    @LastModifiedDate
    private LocalDateTime updatedAt;

}
