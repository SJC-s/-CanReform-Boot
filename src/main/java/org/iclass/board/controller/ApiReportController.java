package org.iclass.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.iclass.board.dto.PostsDTO;
import org.iclass.board.dto.ReportDetailDTO;
import org.iclass.board.dto.ReportsDTO;
import org.iclass.board.entity.PostsEntity;
import org.iclass.board.entity.ReportsEntity;
import org.iclass.board.repository.PostsRepository;
import org.iclass.board.repository.ReportRepository;
import org.iclass.board.service.PostsService;
import org.iclass.board.service.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
@Slf4j

public class ApiReportController {

    private final ReportService reportService;
    private final PostsService postsService;
    private final ReportRepository reportRepository;
    private final PostsRepository postsRepository;

    @GetMapping(produces = "application/json")  // JSON 형식으로 응답
    public ResponseEntity<?> getPostsWithUsers(
            @RequestParam(defaultValue = "0") int reportCount
    ) {
        List<PostsEntity> entity = reportService.getReportList(reportCount);
        return ResponseEntity.ok(entity);
    }

/*    @GetMapping(value = "/details/{postId}", produces = "application/json")
    public ResponseEntity<?> getReportDetails(@PathVariable long postId) {
        Map<PostsDTO, List<ReportsDTO>> map = reportService.getReportDetail(postId);
        return ResponseEntity.ok(map);
    }*/

    @GetMapping(value = "/details/{postId}", produces = "application/json")
    public ResponseEntity<List<ReportDetailDTO>> getReportDetails(@PathVariable long postId) {
        PostsEntity post = postsRepository.findByPostId(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        PostsDTO pDTO = PostsDTO.of(post);

        // 여러 리포트를 가져오는 메서드 사용
        List<ReportsEntity> reports = reportRepository.findByPostId(postId);
        List<ReportsDTO> rDTOs = reports.stream()
                .map(ReportsDTO::of)
                .collect(Collectors.toList());

        // 응답을 위한 DTO
        ReportDetailDTO response = new ReportDetailDTO(pDTO, rDTOs);
        return ResponseEntity.ok(Collections.singletonList(response)); // List로 반환
    }


    @GetMapping("/{postId}")
    public ResponseEntity<?> getReportCount(@PathVariable Long postId) {
        PostsDTO dto = postsService.getPostDetail(postId);

        return ResponseEntity.ok(dto.getReportCount());
    }

    @PostMapping("/addReport/{postId}")
    public ResponseEntity<?> addReport(@PathVariable Long postId, @RequestBody ReportsDTO dto, @AuthenticationPrincipal UserDetails userDetails) throws NotFoundException {

        dto.setUserId(userDetails.getUsername());
        // 신고 내용을 등록
        reportService.saveReport(dto);
        return ResponseEntity.ok(dto);
    }
}
