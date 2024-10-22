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
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
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
        log.info("dkfjdlfjdlf : {}",dto.toString());
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
    public ResponseEntity<?> createPost(@RequestPart("post") PostsDTO postDTO, @RequestPart("files") List<MultipartFile> files, @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        postDTO.setUserId(userDetails.getUsername());  // userId를 DTO에 설정

        List<String> savedFilePaths = new ArrayList<>();
        // 파일 저장 처리
        for (MultipartFile file : files) {
            String filePath = "C:/upload/" + file.getOriginalFilename(); // 파일 경로 지정
            File dest = new File(filePath);
            file.transferTo(dest);
            savedFilePaths.add(file.getOriginalFilename()); // 저장된 파일 리스트에 추가
            log.info("파일 업로드 완료: {}", filePath);
        }

        // PostsDTO에서 파일 경로를 설정 (추가적인 로직이 필요할 수 있음)
        postDTO.setFilenames(String.join(",", savedFilePaths));

        PostsDTO savedPost = postsService.createPost(postDTO);
        return ResponseEntity.ok(savedPost);
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<?> downloadFile(@PathVariable String filename) {
        String uploadDir = "C:/upload/";
        try {
            // 파일 경로 설정
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            // 파일이 존재하고 읽을 수 있는지 확인
            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("파일을 찾을 수 없거나 읽을 수 없습니다.");
            }

            // 파일 다운로드를 위한 응답 설정
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

