package org.iclass.board.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.dao.PostsMapper;
import org.iclass.board.dto.PostsDTO;
import org.iclass.board.entity.PostsEntity;
import org.iclass.board.repository.PostsRepository;
import org.iclass.board.repository.ReportRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PostsService {

    /* myBatis 구간 */
    private final PostsMapper postsMapper;
    private final ReportRepository reportRepository;

    public void savePost(PostsDTO post) {
        postsMapper.savePost(post);
    }


    /* JPA 구간 */
    private final PostsRepository postsRepository;

    public List<Object[]> list() {
        return postsRepository.getPostsWithUsers();
    }

    public Page<PostsDTO> getFilteredPosts(Pageable pageable, String search, String category, String searchClass) {
        Page<PostsEntity> postsPage;

        // 전체 검색 (title, content, userId)
        if ("all".equals(searchClass)) {
            postsPage = postsRepository.findByCategoryAndTitleOrUserIdOrContentContainingOrderByCreatedAtDesc(pageable, category, search);
        }
        // 제목으로 검색
        else if ("title".equals(searchClass)) {
            postsPage = postsRepository.findByCategoryAndTitleContainingOrderByCreatedAtDesc(pageable, category, search);
        }
        // 내용으로 검색
        else if ("content".equals(searchClass)) {
            postsPage = postsRepository.findByCategoryAndContentContainingOrderByCreatedAtDesc(pageable, category, search);
        }
        // 작성자로 검색
        else if ("userId".equals(searchClass)) {
            postsPage = postsRepository.findByCategoryAndUserIdContainingOrderByCreatedAtDesc(pageable, category, search);
        }
        // 예외 처리: 잘못된 searchClass 값이 들어온 경우
        else {
            throw new IllegalArgumentException("Invalid searchClass: " + searchClass);
        }

        return postsPage.map(PostsDTO::of);
    }

    public PostsDTO getPostDetail(Long postId) {
        postsRepository.updateReadCountPlus(postId);
        PostsEntity post = postsRepository.findByPostId(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        log.info("조회수 증가 = {}", post.getReadCount());
        return PostsDTO.of(post);
    }


    public PostsDTO createPost(PostsDTO postsDTO) {
        PostsEntity post = postsDTO.toEntity();
        postsRepository.save(post);
        return PostsDTO.of(post);
    }

    public PostsDTO updatePost(PostsDTO postsDTO, List<MultipartFile> files, List<String> existingFiles) throws IOException {
        PostsEntity existingPost = postsRepository.findByPostId(postsDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        // 업데이트할 필드만 변경
        existingPost.setTitle(postsDTO.getTitle());
        existingPost.setContent(postsDTO.getContent());
        existingPost.setCategory(postsDTO.getCategory());
        existingPost.setStatus(postsDTO.getStatus());
        existingPost.setIsPrivate(postsDTO.getIsPrivate());

        // 기존 파일 처리
        List<String> currentFiles = existingPost.getFilenames() != null ? Arrays.asList(existingPost.getFilenames().split(",")) : new ArrayList<>();
        List<String> updatedFiles = existingFiles != null ? new ArrayList<>(existingFiles) : new ArrayList<>();

        // 기존 파일 중 제거된 파일 삭제 처리
        currentFiles.stream()
                .filter(file -> !updatedFiles.contains(file))
                .forEach(this::deleteFile); // 파일 삭제 로직 호출

        // 새로운 파일 저장
        if (files != null && !files.isEmpty()) {
            List<String> savedFilePaths = saveFiles(files);
            updatedFiles.addAll(savedFilePaths);
        }

        // 업데이트된 파일명 리스트를 저장
        existingPost.setFilenames(String.join(",", updatedFiles));
        postsRepository.save(existingPost);
        return PostsDTO.of(existingPost);
    }

    public void deletePost(Long postId) {
        try {
            PostsEntity post = postsRepository.findByPostId(postId)
                    .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));

            // 삭제 진행
            postsRepository.delete(post);
            log.info("게시글 삭제 완료: ID = {}", postId);
        } catch (Exception e) {
            log.error("게시글 삭제 중 오류 발생. ID = {}", postId, e);
            throw new RuntimeException("게시글 삭제 중 오류가 발생했습니다. ID: " + postId, e);
        }
    }

    public List<String> saveFiles(List<MultipartFile> files) throws IOException {
        List<String> savedFilePaths = new ArrayList<>();
        // 허용할 확장자 목록
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "gif");

        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();
            if (filename == null) {
                throw new IllegalArgumentException("파일 이름을 찾을 수 없습니다.");
            }

            // 파일 확장자 확인
            String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
            if (!allowedExtensions.contains(extension)) {
                throw new IllegalArgumentException("허용되지 않은 파일 확장자입니다. 허용된 확장자: " + String.join(", ", allowedExtensions));
            }

            // 파일명 인코딩(띄어쓰기 정규화)
            String encodedFilename = URLEncoder.encode(filename.replaceAll("[%\\s+]", "_"), StandardCharsets.UTF_8);

            // 파일 저장 경로 지정
            String filePath = "C:/upload/" + encodedFilename;
            File dest = new File(filePath);
            file.transferTo(dest); // 파일 저장

            savedFilePaths.add(encodedFilename);
            log.info("파일 업로드 완료: {}", encodedFilename);
        }
        return savedFilePaths;
    }

    public void deleteFile(String filename) {
        try {
            String decodedFilename = URLDecoder.decode(filename, StandardCharsets.UTF_8);
            String filePath = "C:/upload/" + decodedFilename;
            File file = new File(filePath);
            if (file.exists()) {
                if (file.delete()) {
                    log.info("파일 삭제 완료: {}", decodedFilename);
                } else {
                    log.warn("파일 삭제 실패: {}", decodedFilename);
                }
            }
        } catch (Exception e) {
            log.error("파일 삭제 중 오류 발생: {}", filename, e);
        }
    }


    // 신고수 증가용 함수
    public void editPost(PostsEntity entity) {
        PostsEntity originEntity = postsRepository.findByPostId(entity.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없음"));
        // postId로 가져온 신고들의 개수를 계산
        Integer reportCount = reportRepository.countByPostId(entity.getPostId());

        // 계산된 신고 수를 대입
        entity.setReportCount(reportCount);

        // 그 외 내용을 대입 (안하면 null로 초기화)
        entity.setTitle(originEntity.getTitle());
        entity.setContent(originEntity.getContent());
        entity.setCategory(originEntity.getCategory());
        entity.setStatus(originEntity.getStatus());
        entity.setIsPrivate(originEntity.getIsPrivate());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setFilenames(originEntity.getFilenames());
        entity.setCreatedAt(originEntity.getCreatedAt());
        postsRepository.save(entity);
    }
}



