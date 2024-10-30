package org.iclass.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.iclass.board.dto.AnnouncementDTO;
import org.iclass.board.service.AnnouncementService;
import org.iclass.board.service.PostsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/announcement")
public class ApiAnnouncementController {
    
    private final AnnouncementService announcementService;

    // 관리자 권한 확인
    private boolean isAdmin(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .noneMatch(auth -> auth.getAuthority().equals("ADMIN"));
    }

    /* JPA 구간 */
    @GetMapping("/latest")
    public ResponseEntity<?> getAnnouncement() {
        log.info("AnnouncementController ::::::: {}", "받음");
        List<AnnouncementDTO> dtoList = announcementService.getAllAnnouncements();
        log.info("AnnouncementController ::::::: {}", dtoList.toString());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{announcementId}")
    public ResponseEntity<?> getAnnouncementDetails(@PathVariable Long announcementId) {
        log.info("AnnouncementController ::::::: {}", announcementId);
        AnnouncementDTO dto = announcementService.getAnnouncement(announcementId);
        log.info("AnnouncementController ::::::: {}", dto.toString());
        return ResponseEntity.ok(dto);
    }

    // 공지 작성
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPost(@RequestPart("post") AnnouncementDTO announcementDTO, @RequestPart(value =  "files", required = false) List<MultipartFile> files, @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        // 관리자 권한 확인
        if(isAdmin(userDetails)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("관리자 권한이 필요합니다.");
        }
        announcementDTO.setUserId(userDetails.getUsername());  // userId를 DTO에 설정
        // 파일 없이도 등록 가능
        if (files != null && !files.isEmpty()) {
            // 파일 저장 로직을 서비스로 위임
            List<String> savedFilePaths = announcementService.saveFiles(files);
            announcementDTO.setFilenames(String.join(",", savedFilePaths));
        }

        AnnouncementDTO savedAnnouncement = announcementService.createAnnouncement(announcementDTO);
        log.info("공지 작성 ::::::: {}", savedAnnouncement);
        return ResponseEntity.ok(savedAnnouncement);
    }

    // 공지 수정
    @PutMapping("/{announcementId}")
    public ResponseEntity<?> updateAnnouncement(
            @PathVariable Long announcementId,
            @RequestPart("announcement") AnnouncementDTO announcementDTO,
            @RequestPart(value = "files", required = false) List<MultipartFile> files, // 새로 업로드된 파일들
            @RequestParam(value = "existingFiles", required = false) List<String> existingFiles, // 기존 파일명 리스트
            @AuthenticationPrincipal UserDetails userDetails) throws IOException, NotFoundException {
        // 관리자 권한 확인
        if(isAdmin(userDetails)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("관리자 권한이 필요합니다.");
        }

        // 수정 처리, 파일도 포함해서 업데이트
        AnnouncementDTO updatedAnnouncement = announcementService.updateAnnouncement(announcementDTO, files, existingFiles);
        return ResponseEntity.ok(updatedAnnouncement);
    }

    // 삭제
    @DeleteMapping("/{announcementId}")
    public ResponseEntity<?> deleteAnnouncement(@PathVariable Long announcementId, @AuthenticationPrincipal UserDetails userDetails) {
        // 관리자 권한 확인
        if(isAdmin(userDetails)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("관리자 권한이 필요합니다.");
        }
        
        // 삭제 처리
        announcementService.deleteAnnouncement(announcementId);
        return ResponseEntity.ok("공지가 삭제되었습니다.");
    }

}

