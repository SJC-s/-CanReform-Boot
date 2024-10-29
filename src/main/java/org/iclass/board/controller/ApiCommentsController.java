package org.iclass.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.dto.CommentsDTO;
import org.iclass.board.service.CommentsService;
import org.iclass.board.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j


public class ApiCommentsController {

    private final CommentsService commentsService;
    private final UserService userService;

    @PostMapping("/comments")
    public ResponseEntity<?> createComment(@RequestBody CommentsDTO commentsDTO) {
        CommentsDTO createComment = commentsService.createComment(commentsDTO);
        return ResponseEntity.ok().body(createComment);
    }

    @GetMapping("/comments/{postId}")
    public ResponseEntity<?> getComments(@PathVariable long postId) {
        List<CommentsDTO> list = commentsService.getComments(postId);
        return ResponseEntity.ok(list);
    }

    // 댓글 개별 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable long commentId, @RequestParam String userId) {
        String writer = commentsService.getUserId(commentId);
        if(writer.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        String role = userService.findUsersroleByUserId(userId);
        if(!writer.equals(userId) && !role.equals("ADMIN")) {
            return ResponseEntity.badRequest().build();
        }
        int result = commentsService.deleteComment(commentId);
        if(result == 1) {
            log.info("삭제 성공");
            return ResponseEntity.ok().build();
        } else {
            log.info("삭제 실패");
            return ResponseEntity.notFound().build();
        }
    }

    // 댓글 모두 삭제
    @DeleteMapping("/comments/deletePost/{postId}")
    public ResponseEntity<?> deleteCommentByPostId(@PathVariable long postId, String userId) {
        String role = userService.findUsersroleByUserId(userId);
        if(!role.equals("ADMIN")) {
            return ResponseEntity.badRequest().build();
        }
        int result = commentsService.deleteComments(postId);
        if(result == 1) {
            log.info("삭제 성공");
        } else {
            log.info("댓글 없음");
        }
        return ResponseEntity.ok().build();
    }

}
