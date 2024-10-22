package org.iclass.board.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DialectOverride;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data     // 불변객체 관련된 메소드 재정의
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert // @DynamicInsert와 @DynamicUpdate는 해당값이 입력되지 않았을때(null), INSERT, UPDATE에서 null인 value를 제외하는 어노테이션
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@Table(name = "POSTS")
public class PostsEntity {

    // JPA 로 테이블을 만들기 위한 클래스
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POSTID")
    private Long postId;

    @Column(name = "USERID", nullable = false)
    private String userId;

    @Column(name = "TITLE", nullable = false, length = 100)
    private String title;

    @Lob
    @Column(name = "CONTENT")
    private String content;

    @Column(name = "ISPRIVATE", nullable = false, length = 1)
    private char isPrivate;

    @Column(name = "CATEGORY", nullable = false, length = 50)
    private String category;

    @Column(name = "FILENAMES")
    private String filenames;

    @Column(name = "READCOUNT")
    @ColumnDefault("0")
    private Integer readCount;

    @Column(name = "COMMENTCOUNT")
    @ColumnDefault("0")
    private Integer commentCount;

    @Column(name = "STATUS", length = 50)
    @ColumnDefault("'OPEN'")
    private String status;

    @Column(name = "CREATEDAT")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "UPDATEDAT")
    private LocalDateTime updatedAt;


}

/*
CREATE TABLE posts (
    postId NUMBER(10) PRIMARY KEY,
    userId VARCHAR2(50) NOT NULL,
    title VARCHAR2(100) NOT NULL,
    content CLOB,
    isPrivate CHAR(1) DEFAULT 'N' CHECK (isPrivate IN ('Y', 'N')),  -- 공개/비공개
    category VARCHAR2(50) DEFAULT 'Inquiry' CHECK (category IN ('Inquiry', 'request')),
    createdAt DATE DEFAULT SYSDATE,
    updatedAt DATE,
    filenames VARCHAR2(255),
    readCount NUMBER(10) DEFAULT 0,
    commentCount NUMBER(10) DEFAULT 0,
    status VARCHAR2(50) NOT NULL CHECK (status IN ('OPEN', 'CLOSED', 'PENDING')),  -- 게시글 상태
    CONSTRAINT postsUserIdFk FOREIGN KEY (userId) REFERENCES users(userId)
);
 */