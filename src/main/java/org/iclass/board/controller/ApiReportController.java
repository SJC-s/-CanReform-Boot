package org.iclass.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.iclass.board.dto.PostsDTO;
import org.iclass.board.dto.ReportsDTO;
import org.iclass.board.service.PostsService;
import org.iclass.board.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
@Slf4j

public class ApiReportController {

    private final ReportService reportService;
    private final PostsService postsService;

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
