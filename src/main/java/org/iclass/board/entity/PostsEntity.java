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
@Data     // 불변객체 관련된 메소드 재정의
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
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

    @Column(name = "FILENAMES", length = 255)
    private String filenames;

    @Column(name = "READCOUNT", nullable = false)
    private Integer readCount;

    @Column(name = "COMMENTCOUNT", nullable = false)
    private Integer commentCount;

    @Column(name = "STATUS", nullable = false, length = 50)
    private String status;

    @Column(name = "CREATEDAT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATEDAT")
    private LocalDateTime updatedAt;


}

/*
CREATE TABLE POSTS (
    POST_ID NUMBER(10) PRIMARY KEY,
    USER_ID NUMBER(10) NOT NULL,
    TITLE VARCHAR2(100) NOT NULL,
    CONTENT CLOB,
    IS_PRIVATE CHAR(1) DEFAULT 'N' CHECK (IS_PRIVATE IN ('Y', 'N')),  -- 공개/비공개
    CATEGORY VARCHAR2(50) NOT NULL DEFAULT 'Inquiry' CHECK(CATEGORY IN('Inquiry', 'request')),
    CREATED_AT DATE DEFAULT SYSDATE,
    UPDATED_AT DATE,
	FILENAMES VARCHAR2(255),
	 READCOUNT NUMBER(10) DEFAULT 0,
	 COMMENTCOUNT NUMBER(10) DEFAULT 0,
    STATUS VARCHAR2(50) NOT NULL CHECK (STATUS IN ('OPEN', 'CLOSED', 'PENDING')),  -- 게시글 상태
    FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID)
);
 */
