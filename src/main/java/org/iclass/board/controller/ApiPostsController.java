package org.iclass.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.service.PostsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiPostsController {

    private final PostsService postsService;

    /* JPA 구간 */
    // user_id -> username 포함하기 위한 데이터
    @GetMapping("/posts")
    public ResponseEntity<?> getPostsWithUsers() {
        List<Object[]> users = postsService.list();
        return ResponseEntity.ok(users);
    }
}

