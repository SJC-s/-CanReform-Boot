package org.iclass.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.iclass.board.dto.PostsDTO;
import org.iclass.board.entity.PostsEntity;
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
    public ResponseEntity<?> addReport(@PathVariable Long postId) throws NotFoundException {
        // dto 가져와서 reportcount 증가시키기
        PostsDTO dto = postsService.getPostDetail(postId);
        log.info(">>>>>> 신고 수 : {}", dto.getReportCount());
        dto.setReportCount(dto.getReportCount() + 1);
        log.info(">>>>>> 증가 신고 수 : {}", dto.getReportCount());

        // 변경된 dto를 entity로 변환 후 데이터베이스에 적용
        postsService.editPost(dto.toEntity());
        log.info(">>>>>>> entity : {}", dto.toEntity());

        return ResponseEntity.ok(dto.getReportCount());
    }
}
