package org.iclass.board.controller;

import org.iclass.board.dto.PostDTO;
import org.iclass.board.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/write")
    public String showWriteForm(Model model) {
        model.addAttribute("post", new PostDTO());
        return "write";  // write.html 로 이동
    }

    @PostMapping("/write")
    public String savePost(PostDTO post) {
        postService.savePost(post);
        return "redirect:/posts";  // 게시물 목록 페이지로 리다이렉트
    }
}
