package org.iclass.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.dto.MainPageWithRatingsDTO;
import org.iclass.board.dto.PostsDTO;
import org.iclass.board.service.PostsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class ApiMainController {
    private final PostsService postsService;

    @GetMapping
    public ResponseEntity<?> getBoards() {
        MainPageWithRatingsDTO boardPage = postsService.getBoardToMain();
        return ResponseEntity.ok(boardPage);
    }

}
