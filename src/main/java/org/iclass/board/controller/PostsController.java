package org.iclass.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.dto.PostsDTO;
import org.iclass.board.service.PostsService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;

    /* myBatis 구간 */
    @GetMapping("/write")
    public String showWriteForm(Model model) {
        model.addAttribute("post", new PostsDTO());
        return "write";  // write.html 로 이동
    }

    @PostMapping("/write")
    public String savePost(PostsDTO post) {
        postsService.savePost(post);
        return "redirect:/posts";  // 게시물 목록 페이지로 리다이렉트
    }



    /* JPA 구간 */
    @GetMapping("/api/posts")
    public ResponseEntity<?> findAll() {

        List<PostsDTO> list = postsService.list();
        return ResponseEntity.ok(list);
    }
}

