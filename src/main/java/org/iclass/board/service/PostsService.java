package org.iclass.board.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.dao.PostsMapper;
import org.iclass.board.dto.PostsDTO;
import org.iclass.board.entity.PostsEntity;
import org.iclass.board.repository.PostsRepository;
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
import java.nio.file.Path;
import java.nio.file.Paths;
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
        PostsEntity post = postsRepository.findByPostId(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        return PostsDTO.of(post);
    }

    public PostsDTO createPost(PostsDTO postsDTO, List<MultipartFile> files) throws IOException {
        // 파일 저장 처리 로직을 서비스 클래스에서 처리
        List<String> savedFilePaths = saveFiles(files);
        postsDTO.setFilenames(String.join(",", savedFilePaths));

        PostsEntity post = postsDTO.toEntity();
        postsRepository.save(post);
        return PostsDTO.of(post);
    }

    public Resource downloadFile(String filename) throws IOException {
        // 파일 경로 설정
        Path filePath = Paths.get("C:/upload/").resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            throw new FileNotFoundException("파일을 찾을 수 없습니다: " + filename);
        }

        return resource;
    }

    public PostsDTO updatePost(PostsDTO postsDTO, List<MultipartFile> files) throws IOException {
        PostsEntity existingPost = postsRepository.findByPostId(postsDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        // 업데이트할 필드만 변경
        existingPost.setTitle(postsDTO.getTitle());
        existingPost.setContent(postsDTO.getContent());
        existingPost.setCategory(postsDTO.getCategory());
        existingPost.setStatus(postsDTO.getStatus());
        existingPost.setIsPrivate(postsDTO.getIsPrivate());

        // 파일 저장 처리 로직을 서비스 클래스에서 처리
        List<String> savedFilePaths = saveFiles(files);

        // 기존 파일 경로에 새 파일 경로 추가
        if (!savedFilePaths.isEmpty()) {
            String existingFiles = existingPost.getFilenames();
            String updatedFiles = (existingFiles == null || existingFiles.isEmpty()) ?
                    String.join(",", savedFilePaths) : existingFiles + "," + String.join(",", savedFilePaths);
            existingPost.setFilenames(updatedFiles);
        }

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
        if (files != null && !files.isEmpty()) {
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

                // 파일 저장 경로 지정
                String filePath = "C:/upload/" + filename;
                File dest = new File(filePath);
                file.transferTo(dest); // 파일 저장

                savedFilePaths.add(filePath);
                log.info("파일 업로드 완료: {}", filePath);
            }
        }
        return savedFilePaths;
    }
}



