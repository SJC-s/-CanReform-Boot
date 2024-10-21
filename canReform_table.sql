DROP TABLE comments;
DROP TABLE ratings;
DROP TABLE reports;
DROP TABLE schedule;
DROP TABLE estimates;
DROP TABLE posts;
DROP TABLE users;

-- 1. Users (회원)
CREATE TABLE users (
    userId VARCHAR2(50) PRIMARY KEY,
    password VARCHAR2(255) NOT NULL,
    username VARCHAR2(50) NOT NULL,
    email VARCHAR2(100) NOT NULL UNIQUE,
    usersRole VARCHAR2(20) DEFAULT 'MEMBER' CHECK (usersRole IN ('MEMBER', 'ADMIN')),  -- 역할: 회원, 관리자
    createdAt DATE DEFAULT SYSDATE,
    isActive CHAR(1) DEFAULT 'Y' CHECK (isActive IN ('Y', 'N'))  -- 활성화 상태
);

-- 2. Posts (게시판 글)
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

-- 3. Comments (댓글)
CREATE TABLE comments (
    commentId NUMBER(10) PRIMARY KEY,
    postId NUMBER(10) NOT NULL,
    userId VARCHAR2(50) NOT NULL,
    content CLOB,
    createdAt DATE DEFAULT SYSDATE,
    CONSTRAINT commentsPostIdFk FOREIGN KEY (postId) REFERENCES posts(postId),
    CONSTRAINT commentsUserIdFk FOREIGN KEY (userId) REFERENCES users(userId)
);

-- 4. Ratings (평가 OR 별점)
CREATE TABLE ratings (
    ratingId NUMBER(10) PRIMARY KEY,
    postId NUMBER(10) NOT NULL,
    userId VARCHAR2(50) NOT NULL,
    rating NUMBER(2) CHECK (rating BETWEEN 1 AND 5),  -- 별점 1-5
    review CLOB,
    createdAt DATE DEFAULT SYSDATE,
    CONSTRAINT ratingsPostIdFk FOREIGN KEY (postId) REFERENCES posts(postId),
    CONSTRAINT ratingsUserIdFk FOREIGN KEY (userId) REFERENCES users(userId)
);

-- 5. Reports (신고)
CREATE TABLE reports (
    reportId NUMBER(10) PRIMARY KEY,
    postId NUMBER(10) NOT NULL,
    userId VARCHAR2(50) NOT NULL,
    reason VARCHAR2(255),
    createdAt DATE DEFAULT SYSDATE,
    CONSTRAINT reportsPostIdFk FOREIGN KEY (postId) REFERENCES posts(postId),
    CONSTRAINT reportsUserIdFk FOREIGN KEY (userId) REFERENCES users(userId)
);

-- 6. Schedule (일정)
CREATE TABLE schedule (
    scheduleId NUMBER(10) PRIMARY KEY,
    userId VARCHAR2(50) NOT NULL,
    postId NUMBER(10),
    eventDate DATE,
    createdAt DATE DEFAULT SYSDATE,
    CONSTRAINT schedulePostIdFk FOREIGN KEY (postId) REFERENCES posts(postId),
    CONSTRAINT scheduleUserIdFk FOREIGN KEY (userId) REFERENCES users(userId)
);

-- 7. Estimates (견적)
CREATE TABLE estimates (
    estimateId NUMBER(10) PRIMARY KEY,
    postId NUMBER(10) NOT NULL,
    userId VARCHAR2(50) NOT NULL,
    estimatedCost NUMBER(12),  -- 예상 비용
    finalCost NUMBER(12),      -- 최종 비용
    status VARCHAR2(50) NOT NULL CHECK (status IN ('PENDING', 'APPROVED', 'COMPLETED', 'REJECTED')),  -- 견적 상태
    createdAt DATE DEFAULT SYSDATE,
    updatedAt DATE,
    CONSTRAINT estimatesPostIdFk FOREIGN KEY (postId) REFERENCES posts(postId),
    CONSTRAINT estimatesUserIdFk FOREIGN KEY (userId) REFERENCES users(userId)
);

-- Users 테이블에 테스트 데이터 10개 삽입
INSERT INTO users (userId, username, email, password, createdAt)
VALUES ('id1', 'user1', 'user1@example.com', 'password1', SYSDATE);

INSERT INTO users (userId, username, email, password, createdAt)
VALUES ('id2', 'user2', 'user2@example.com', 'password2', SYSDATE);

INSERT INTO users (userId, username, email, password, createdAt)
VALUES ('id3', 'user3', 'user3@example.com', 'password3', SYSDATE);

INSERT INTO users (userId, username, email, password, createdAt)
VALUES ('id4', 'user4', 'user4@example.com', 'password4', SYSDATE);

INSERT INTO users (userId, username, email, password, createdAt)
VALUES ('id5', 'user5', 'user5@example.com', 'password5', SYSDATE);

-- userId 6부터 10까지 추가 생성
BEGIN
    FOR i IN 6..10 LOOP
        INSERT INTO users (userId, username, email, password, createdAt)
        VALUES ('id' || i, 'user' || i, 'user' || i || '@example.com', 'password' || i, SYSDATE);
    END LOOP;
END;
/

-- Posts 테이블에 50개의 테스트 데이터 삽입
INSERT INTO posts (postId, userId, title, content, isPrivate, category, createdAt, updatedAt, filenames, readCount, commentCount, status)
VALUES (1, 'id1', '첫 번째 게시글', '이것은 테스트 콘텐츠입니다.', 'N', 'Inquiry', SYSDATE, SYSDATE, 'file1.txt', 10, 5, 'OPEN');

INSERT INTO posts (postId, userId, title, content, isPrivate, category, createdAt, updatedAt, filenames, readCount, commentCount, status)
VALUES (2, 'id2', '두 번째 게시글', '이것은 테스트 콘텐츠입니다.', 'Y', 'request', SYSDATE, SYSDATE, 'file2.txt', 20, 10, 'CLOSED');

-- 나머지 48개의 레코드 생성
BEGIN
    FOR i IN 3..50 LOOP
        INSERT INTO posts (postId, userId, title, content, isPrivate, category, createdAt, updatedAt, filenames, readCount, commentCount, status)
        VALUES (i, 'id' || TO_CHAR(MOD(i, 10) + 1), '게시글 ' || TO_CHAR(i), '이것은 테스트 콘텐츠입니다. 번호: ' || TO_CHAR(i), 
                CASE WHEN MOD(i, 2) = 0 THEN 'Y' ELSE 'N' END, 
                CASE WHEN MOD(i, 3) = 0 THEN 'request' ELSE 'Inquiry' END,
                SYSDATE - MOD(i, 7), SYSDATE, 
                'file' || TO_CHAR(i) || '.txt', 
                MOD(i, 100), MOD(i, 50), 
                CASE WHEN MOD(i, 4) = 0 THEN 'PENDING' WHEN MOD(i, 4) = 1 THEN 'OPEN' ELSE 'CLOSED' END);
    END LOOP;
END;
/

-- 선택 쿼리
SELECT * FROM comments;
SELECT * FROM ratings;
SELECT * FROM reports;
SELECT * FROM schedule;
SELECT * FROM estimates;
SELECT * FROM posts;
SELECT * FROM users;
