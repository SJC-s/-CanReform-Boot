package org.iclass.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.dto.PostsDTO;
import org.iclass.board.service.PostsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class ApiPostsController {

    private final PostsService postsService;

    /* JPA 구간 */
    // user_id -> username 포함하기 위한 데이터
    @GetMapping
    public ResponseEntity<?> getPostsWithUsers(
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
    public ResponseEntity<?> createPost(@RequestPart("post") PostsDTO postDTO, @RequestPart(value =  "files", required = false) List<MultipartFile> files, @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        postDTO.setUserId(userDetails.getUsername());  // userId를 DTO에 설정

        List<String> savedFilePaths = new ArrayList<>();

        // 카테고리가 "Inquiry"인 경우 파일 없이도 등록 가능
        if (!files.isEmpty() || !"Inquiry".equals(postDTO.getCategory())) {

            // 허용할 확장자 목록
            List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "gif");

            // 파일 저장 처리
            for (MultipartFile file : files) {
                String filename = file.getOriginalFilename();
                if (filename == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("파일 이름을 찾을 수 없습니다.");
                }

                // 파일 확장자 확인
                String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
                if (!allowedExtensions.contains(extension)) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("허용되지 않은 파일 확장자입니다. 허용된 확장자: " + String.join(", ", allowedExtensions));
                }

                // 파일 저장 경로 지정
                String filePath = "C:/upload/" + filename;
                File dest = new File(filePath);
                file.transferTo(dest); // 파일 저장

                savedFilePaths.add(filePath);
                log.info("파일 업로드 완료: {}", filePath);
            }
        }

        // PostsDTO에서 파일 경로를 설정 (추가적인 로직이 필요할 수 있음)
        postDTO.setFilenames(String.join(",", savedFilePaths));

        PostsDTO savedPost = postsService.createPost(postDTO);
        return ResponseEntity.ok(savedPost);
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<?> downloadFile(@PathVariable String filename) throws IOException  {
        // 파일 경로 설정
        Path filePath = Paths.get("C:/upload/").resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            throw new FileNotFoundException("파일을 찾을 수 없습니다: " + filename);
        }

        // 파일의 MIME 타입 추정
        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";  // 기본 MIME 타입
        }

        // Content-Disposition 설정: 다운로드할 때 파일명을 지정
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}

