package org.iclass.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.iclass.board.dto.PostsDTO;
import org.iclass.board.dto.ReportsDTO;
import org.iclass.board.entity.PostsEntity;
import org.iclass.board.service.PostsService;
import org.iclass.board.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
@Slf4j

public class ApiReportController {

    private final ReportService reportService;
    private final PostsService postsService;

    @GetMapping(produces = "application/json")  // JSON 형식으로 응답
    public ResponseEntity<?> getPostsWithUsers(
            @RequestParam(defaultValue = "0") int reportCount
    ) {
        List<PostsEntity> entity = reportService.getReportList(reportCount);
        return ResponseEntity.ok(entity);
    }

    @GetMapping(value = "/details/{postId}", produces = "application/json")
    public ResponseEntity<?> getReportDetails(@PathVariable long postId) {
        Map<PostsDTO, List<ReportsDTO>> map = reportService.getReportDetail(postId);
        return ResponseEntity.ok(map);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getReportCount(@PathVariable Long postId) {
        PostsDTO dto = postsService.getPostDetail(postId);

        return ResponseEntity.ok(dto.getReportCount());
    }

    @PostMapping("/addReport/{postId}")
    public ResponseEntity<?> addReport(@PathVariable Long postId, @RequestBody ReportsDTO dto) throws NotFoundException {
        // 신고 내용을 등록
        reportService.saveReport(dto);
        return ResponseEntity.ok(dto);
    }
}
