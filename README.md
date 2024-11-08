# CanReform - Backend
CanReform의 백엔드는 **Spring Boot**로 구축되어 RESTful API를 통해 프론트엔드와 통신하며, 의류 리폼 업체와 고객 간의 데이터 관리를 효율적으로 지원합니다.

## 프로젝트 구성원
![image](https://github.com/user-attachments/assets/309e2675-a96e-41f3-baf8-2829a8f612a2)

## 개발 일정
![image](https://github.com/user-attachments/assets/bcfb5d5f-fe85-4d13-8b87-1e923fbf3557)


## 📎 프로젝트 개요
의류 리폼 업체가 의뢰 요청을 관리하고, 고객의 평가와 피드백을 통해 서비스 품질을 향상시킬 수 있는 플랫폼입니다.

- **프로젝트 목표**: 안전하고 효율적인 의류 리폼 의뢰 및 관리 시스템 제공
- **주요 기능**: JWT 인증, 게시글 관리, 별점 평가, 관리자 기능(구현 중)

## 🔧 기술 스택
![image](https://github.com/user-attachments/assets/960e385f-0bc9-4a7f-86ec-282c191c7258)

- **프레임워크**: Spring Boot
- **데이터베이스**: Oracle DB
- **ORM**: JPA(MyBatis는 최종적으로 사용 안함)
- **보안**: Spring Security, JWT, Google OAuth
- **빌드 툴**: Gradle

## 📑 주요 기능

1. **사용자 인증**
    - JWT 토큰 발급 및 인증
    - Google OAuth를 통한 소셜 로그인 지원
2. **게시판 API**
    - 게시글 CRUD (작성, 조회, 수정, 삭제)
    - 공개/비공개 게시글 설정 및 접근 권한 제어
3. **댓글 및 별점 기능**
    - 게시글에 대한 댓글 작성 및 관리
    - 별점 평가 기능을 통한 서비스 품질 평가 지원
4. **관리자 기능 (추가 예정)**
    - 최종 견적 산출 및 게시글 마감 처리
    - 웹사이트 운영 상태 모니터링
5. **신고 및 알림 기능 (추가 예정)**
    - 부적절한 게시글 신고 및 알림 시스템



## 📂 프로젝트 구조
```bash
CanReform-Boot
├─ .gitignore                       # Git에서 무시할 파일 및 폴더 목록
├─ canReform_table.sql              # 프로젝트의 데이터베이스 테이블 생성 스크립트
├─ gradle                           # Gradle Wrapper 관련 파일
│  └─ wrapper
│     ├─ gradle-wrapper.jar         # Gradle Wrapper JAR 파일
│     └─ gradle-wrapper.properties  # Gradle Wrapper 설정 파일
├─ gradlew                          # Unix용 Gradle 실행 파일
├─ gradlew.bat                      # Windows용 Gradle 실행 파일
└─ src                              # 소스 코드 폴더
   ├─ main                          # 애플리케이션 메인 코드 디렉토리
   │  ├─ java
   │  │  └─ org
   │  │     └─ iclass
   │  │        └─ board
   │  │           ├─ advice
   │  │           │  └─ RestAdvice.java                  # 예외 처리를 위한 글로벌 예외 처리 핸들러
   │  │           ├─ CanReformApplication.java           # Spring Boot 애플리케이션의 시작점
   │  │           ├─ config                              # 애플리케이션 설정 파일 폴더
   │  │           │  ├─ AppConfig.java                   # 공통 애플리케이션 설정 클래스
   │  │           │  ├─ JpaConfig.java                   # JPA 관련 설정 클래스
   │  │           │  ├─ SecurityConfig.java              # Spring Security 설정 클래스
   │  │           │  └─ WebMvcConfig.java                # 웹 MVC 설정 클래스
   │  │           ├─ controller                          # REST API 엔드포인트 정의 컨트롤러 폴더
   │  │           │  ├─ ApiAnnouncementController.java   # 공지사항 관련 API 컨트롤러
   │  │           │  ├─ ApiAuthController.java           # 인증 관련 API 컨트롤러
   │  │           │  ├─ ApiCommentsController.java       # 댓글 관련 API 컨트롤러
   │  │           │  ├─ ApiMainController.java           # 메인 페이지 관련 API 컨트롤러
   │  │           │  ├─ ApiPostsController.java          # 게시글 관련 API 컨트롤러
   │  │           │  ├─ ApiRatingsController.java        # 별점 평가 관련 API 컨트롤러
   │  │           │  ├─ ApiReportController.java         # 신고 관련 API 컨트롤러
   │  │           │  └─ ApiUserController.java           # 사용자 관련 API 컨트롤러
   │  │           ├─ dto                                 # 데이터 전송 객체(DTO) 폴더
   │  │           │  ├─ AnnouncementDTO.java             # 공지사항 데이터 전송 객체
   │  │           │  ├─ AvgRatingDTO.java                # 평균 평점 데이터 전송 객체
   │  │           │  ├─ CommentsDTO.java                 # 댓글 데이터 전송 객체
   │  │           │  ├─ EstimatesDTO.java                # 견적 데이터 전송 객체
   │  │           │  ├─ MainPageWithRatingsDTO.java      # 메인 페이지 및 평점 데이터 전송 객체
   │  │           │  ├─ PostsDTO.java                    # 게시글 데이터 전송 객체
   │  │           │  ├─ RatingsDTO.java                  # 별점 데이터 전송 객체
   │  │           │  ├─ ReportDetailDTO.java             # 신고 상세 데이터 전송 객체
   │  │           │  ├─ ReportsDTO.java                  # 신고 데이터 전송 객체
   │  │           │  ├─ ScheduleDTO.java                 # 일정 데이터 전송 객체
   │  │           │  ├─ UserAccountDTO.java              # 사용자 계정 데이터 전송 객체
   │  │           │  └─ UsersDTO.java                    # 사용자 데이터 전송 객체
   │  │           ├─ entity                              # 데이터베이스 엔티티 클래스 폴더
   │  │           │  ├─ AnnouncementEntity.java          # 공지사항 엔티티
   │  │           │  ├─ CommentsEntity.java              # 댓글 엔티티
   │  │           │  ├─ EstimatesEntity.java             # 견적 엔티티
   │  │           │  ├─ PasswordResetToken.java          # 비밀번호 재설정 토큰 엔티티
   │  │           │  ├─ PostsEntity.java                 # 게시글 엔티티
   │  │           │  ├─ RatingsEntity.java               # 별점 엔티티
   │  │           │  ├─ ReportsEntity.java               # 신고 엔티티
   │  │           │  ├─ ScheduleEntity.java              # 일정 엔티티
   │  │           │  └─ UsersEntity.java                 # 사용자 엔티티
   │  │           ├─ jwt                                 # JWT 관련 클래스 폴더
   │  │           │  ├─ JwtAuthenticationFilter.java     # JWT 인증 필터
   │  │           │  ├─ JwtAuthorizationFilter.java      # JWT 인가 필터
   │  │           │  ├─ JwtUtil.java                     # JWT 유틸리티 클래스
   │  │           │  ├─ MemberLoginRequestDTO.java       # 멤버 로그인 요청 DTO
   │  │           │  ├─ TokenProvider.java               # JWT 토큰 제공 클래스
   │  │           │  └─ TokenResponseDTO.java            # 토큰 응답 DTO
   │  │           ├─ repository                          # 데이터 접근 객체(DAO) 폴더
   │  │           │  ├─ AnnouncementRepository.java      # 공지사항 리포지토리
   │  │           │  ├─ CommentsRepository.java          # 댓글 리포지토리
   │  │           │  ├─ PasswordResetTokenRepository.java # 비밀번호 재설정 토큰 리포지토리
   │  │           │  ├─ PostsRepository.java             # 게시글 리포지토리
   │  │           │  ├─ RatingsRepository.java           # 별점 리포지토리
   │  │           │  ├─ ReportRepository.java            # 신고 리포지토리
   │  │           │  └─ UserRepository.java              # 사용자 리포지토리
   │  │           └─ service                             # 서비스 레이어 폴더
   │  │              ├─ AnnouncementService.java         # 공지사항 서비스
   │  │              ├─ CommentsService.java             # 댓글 서비스
   │  │              ├─ CustomUserDetailsService.java     # 사용자 인증 서비스
   │  │              ├─ PostsService.java                # 게시글 서비스
   │  │              ├─ RatingsService.java              # 별점 서비스
   │  │              ├─ ReportService.java               # 신고 서비스
   │  │              └─ UserService.java                 # 사용자 서비스
   │  └─ resources
   │     ├─ application.properties                      # 애플리케이션 설정 파일
   │     ├─ mapper                                     # MyBatis 매퍼 XML 폴더
   │     │  ├─ post.xml                                # 게시글 관련 SQL 매퍼
   │     │  └─ user.xml                                # 사용자 관련 SQL 매퍼
   │     ├─ static                                     # 정적 파일 폴더 (CSS, JS 등)
   │     │  ├─ css
   │     │  │  └─ styles.css                           # 스타일시트
   │     │  └─ js
   │     │     ├─ login.js                             # 로그인 페이지용 JavaScript 파일
   │     │     └─ signup.js                            # 회원가입 페이지용 JavaScript 파일
   │     └─ templates                                  # HTML 템플릿 폴더
   │        ├─ editMyPage.html                         # 마이페이지 편집 템플릿
   │        ├─ index.html                              # 메인 페이지 템플릿
   │        └─ mypage.html                             # 마이페이지 템플릿
   └─ test                                            # 테스트 코드 폴더
      └─ java
         └─ org
            └─ iclass
               └─ board
                  ├─ CanReformApplicationTests.java    # 애플리케이션 테스트 파일
                  ├─ dao
                  │  └─ UserMapperTest.java            # 사용자 매퍼 테스트
                  ├─ jwt
                  │  ├─ JwtKeyGenerator.java           # JWT 키 생성 테스트 클래스
                  │  └─ TokenProviderTest.java         # 토큰 제공자 테스트 클래스
                  └─ repository
                     └─ PostsRepositoryTest.java       # 게시글 리포지토리 테스트 클래스
 ```

## 🗃️ ER 다이어그램
<img width="994" alt="DB 테이블 구조" src="https://github.com/user-attachments/assets/d1685250-87c3-4f1d-8c88-e5ecb7207cd4">

## API 명세서
(URL : https://sumptuous-nerine-fd8.notion.site/CanReform-API-136fd37a682e80f0b190ed6789399d31?pvs=4)
![image](https://github.com/user-attachments/assets/5983837e-f1b8-4060-8934-f300596edb4d)
![image](https://github.com/user-attachments/assets/cdf504e5-09ab-458c-83de-595c35898d44)
![image](https://github.com/user-attachments/assets/8dc13d69-8c15-4443-9952-822a16ab6c3e)
![image](https://github.com/user-attachments/assets/e471ff6e-cdc8-4420-8aa4-6fd7865daa7d)
![image](https://github.com/user-attachments/assets/675e684d-3e93-4a80-bb02-acc485dcb295)
![image](https://github.com/user-attachments/assets/3fca7f51-061b-4f4f-9113-8c97d0c8d0dd)
![image](https://github.com/user-attachments/assets/4352455c-0152-4af2-bb0b-409b726773fa)



## 향후 계획 및 개선 사항
![image](https://github.com/user-attachments/assets/3eef5eac-9c84-430d-99b0-04661cb797c3)


