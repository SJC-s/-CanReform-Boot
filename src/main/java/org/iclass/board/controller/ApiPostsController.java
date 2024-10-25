package org.iclass.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.dto.PostsDTO;
import org.iclass.board.service.PostsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class ApiPostsController {

    private final PostsService postsService;

    /* JPA 구간 */
    @GetMapping
    public ResponseEntity<?> getPostsSearch(
        @RequestParam(defaultValue = "1") int page,  // 페이지 번호 (기본값 1)
        @RequestParam(defaultValue = "5") int limit, // 페이지 당 게시글 수 (기본값 5)
        @RequestParam(defaultValue = "") String search,  // 검색어 (기본값 "")
        @RequestParam(defaultValue = "all") String category,  // 카테고리 필터 (기본값 "all")
        @RequestParam(defaultValue = "all") String searchClass // 검색어 필터 (기본값 "all")
    ) {
        if (page < 1) {
            throw new IllegalArgumentException("Page index must not be less than 1");
        }
        Pageable pageable = PageRequest.of(page - 1, limit);  // 페이지 번호를 0 기반으로 변환
        Page<PostsDTO> dto = postsService.getFilteredPosts(pageable, search, category, searchClass);
        return ResponseEntity.ok(dto);
    }

    // 게시글 상세 조회
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostDetail(@PathVariable Long postId) {
        PostsDTO dto = postsService.getPostDetail(postId);
        return ResponseEntity.ok(dto);
    }

    // 게시글 작성
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPost(@RequestPart("post") PostsDTO postsDTO, @RequestPart(value =  "files", required = false) List<MultipartFile> files, @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        postsDTO.setUserId(userDetails.getUsername());  // userId를 DTO에 설정

        // 카테고리가 "Inquiry"인 경우 파일 없이도 등록 가능
        if (!"Inquiry".equals(postsDTO.getCategory()) || files != null) {
            // 파일 저장 로직을 서비스로 위임
            List<String> savedFilePaths = postsService.saveFiles(files);
            postsDTO.setFilenames(String.join(",", savedFilePaths));
        }

        PostsDTO savedPost = postsService.createPost(postsDTO);
        return ResponseEntity.ok(savedPost);
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<?> downloadFile(@PathVariable String filename) throws IOException {
        String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8);

        // 파일 경로 설정
        Path filePath = Paths.get("C:/upload/").resolve(encodedFilename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            throw new FileNotFoundException("파일을 찾을 수 없습니다: " + encodedFilename);
        }

        // 파일의 MIME 타입 추정
        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";  // 기본 MIME 타입
        }

        String decodedFilename = "";
        if (resource.getFilename() != null) decodedFilename = URLDecoder.decode(resource.getFilename(), StandardCharsets.UTF_8);

        // Content-Disposition 설정: 다운로드할 때 파일명을 지정
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + decodedFilename + "\"")
                .body(resource);
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePost(
            @PathVariable Long postId,
            @RequestPart("post") PostsDTO postsDTO,
            @RequestPart(value = "files", required = false) List<MultipartFile> files, // 새로 업로드된 파일들
            @RequestParam(value = "existingFiles", required = false) List<String> existingFiles, // 기존 파일명 리스트
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        // 로그인된 사용자와 게시글 작성자 확인
        PostsDTO existingPost = postsService.getPostDetail(postId);
        if (!existingPost.getUserId().equals(userDetails.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("게시글을 수정할 권한이 없습니다.");
        }

        // 수정 처리, 파일도 포함해서 업데이트
        PostsDTO updatedPost = postsService.updatePost(postsDTO, files, existingFiles);
        return ResponseEntity.ok(updatedPost);
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {
        // 로그인된 사용자와 게시글 작성자 확인
        PostsDTO existingPost = postsService.getPostDetail(postId);
        if (!existingPost.getUserId().equals(userDetails.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("게시글을 삭제할 권한이 없습니다.");
        }

        // 삭제 처리
        postsService.deletePost(postId);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }

}

