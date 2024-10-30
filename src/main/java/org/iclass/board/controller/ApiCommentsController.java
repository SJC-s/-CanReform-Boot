package org.iclass.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.dto.CommentsDTO;
import org.iclass.board.service.CommentsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j


public class ApiCommentsController {

    private final CommentsService commentsService;

    @PostMapping("/comments/postComments")
    public ResponseEntity<?> createComment(@RequestBody CommentsDTO commentsDTO, @RequestParam Long postId) {
        CommentsDTO createComment = commentsService.createComment(commentsDTO);
        createComment.setPostId(postId);
        return ResponseEntity.ok().body(createComment);
    }

    @PostMapping("/comments/announcementComments")
    public ResponseEntity<?> createAnnouncementComment(@RequestBody CommentsDTO commentsDTO, @RequestParam Long announcementId) {
        CommentsDTO createComment = commentsService.createComment(commentsDTO);
        createComment.setAnnouncementId(announcementId);
        return ResponseEntity.ok().body(createComment);
    }

    @GetMapping("/comments/postComments/{postId}")
    public ResponseEntity<?> getPostComments(@PathVariable long postId) {
        List<CommentsDTO> list = commentsService.getPostComments(postId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/comments/announcementComments/{announcementId}")
    public ResponseEntity<?> getAnnouncementComments(@PathVariable Long announcementId) {
        List<CommentsDTO> list = commentsService.getAnnouncementComments(announcementId);
        return ResponseEntity.ok(list);
    }

    // 댓글 개별 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetails userDetails) {
        String writer = commentsService.getUserId(commentId);
        String username = userDetails.getUsername();
        if(writer.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        String role = userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))? "ADMIN" : "MEMBER";
        if(!writer.equals(username) && !role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("댓글 삭제 권한이 없습니다.");
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

    // 댓글 모두 삭제 (게시글)
    @DeleteMapping("/comments/deletePost/{postId}")
    public ResponseEntity<?> deleteCommentByPostId(@PathVariable long postId, @AuthenticationPrincipal UserDetails userDetails) {
        String role = userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))? "ADMIN" : "MEMBER";
        if(!role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("댓글 삭제 권한이 없습니다.");
        }
        int result = commentsService.deletePostComments(postId);
        if(result == 1) {
            log.info("삭제 성공");
        } else {
            log.info("댓글 없음");
        }
        return ResponseEntity.ok().build();
    }

    // 댓글 모두 삭제 (공지사항)
    @DeleteMapping("/comments/deleteAnnouncement/{announcementId}")
    public ResponseEntity<?> deleteCommentByAnnouncementId(@PathVariable Long announcementId, @AuthenticationPrincipal UserDetails userDetails){
        String role = userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN"))? "ADMIN" : "MEMBER";
        if(!role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("댓글 삭제 권한이 없습니다.");
        }
        int result = commentsService.deleteAnnouncementComments(announcementId);
        if(result == 1) {
            log.info("삭제 성공");
        } else {
            log.info("댓글 없음");
        }
        return ResponseEntity.ok().build();
    }

}
