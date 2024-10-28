package org.iclass.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.dto.CommentsDTO;
import org.iclass.board.service.CommentsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j


public class ApiCommentsController {

    private final CommentsService commentsService;

    @PostMapping("/comments")
    public ResponseEntity<?> createComment(@RequestBody CommentsDTO commentsDTO) {
        log.info("::::::::ApiCommentsCotroller.createComment : {}", commentsDTO.toString());
        CommentsDTO createComment = commentsService.createComment(commentsDTO);
        log.info("::::::::ApiCommentsCotroller.createComment : {}", createComment.toString());
        return ResponseEntity.ok().body(createComment);
    }

    @GetMapping("/comments/{postId}")
    public ResponseEntity<?> getComments(@PathVariable long postId) {
        List<CommentsDTO> list = commentsService.getComments(postId);
        log.info("::::::::ApiCommentsCotroller.getComments : {}", list.toString());
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable long commentId) {
        log.info("::::::::ApiCommentsCotroller.deleteComment : {}", commentId);
        int result = commentsService.deleteComment(commentId);
        if(result == 1) {
            log.info("삭제 성공");
            return ResponseEntity.ok().build();
        } else {
            log.info("삭제 실패");
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/comments/deletePost/{postId}")
    public ResponseEntity<?> deleteCommentByPostId(@PathVariable long postId) {
        int result = commentsService.deleteComments(postId);
        if(result == 1) {
            log.info("삭제 성공");
        } else {
            log.info("댓글 없음");
        }
        return ResponseEntity.ok().build();
    }

}
