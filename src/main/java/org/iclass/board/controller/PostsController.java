package org.iclass.board.controller;

import lombok.RequiredArgsConstructor;
import org.iclass.board.dto.PostsDTO;
import org.iclass.board.service.PostsService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
}
