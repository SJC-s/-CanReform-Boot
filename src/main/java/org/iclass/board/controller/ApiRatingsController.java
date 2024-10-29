package org.iclass.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iclass.board.dto.AvgRatingDTO;
import org.iclass.board.dto.RatingsDTO;
import org.iclass.board.service.RatingsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/ratings")
public class ApiRatingsController {

    private final RatingsService ratingsService;

    // 1. 사용자 개인 평점 가져오기
    @GetMapping("/{postId}/user/{userId}")
    public ResponseEntity<?> getUserRating(
            @PathVariable Long postId,
            @PathVariable String userId) {
        RatingsDTO userRating = ratingsService.getUserRating(postId, userId);
        return ResponseEntity.ok(userRating);
    }

    // 2. 게시물의 평균 평점 가져오기
    @GetMapping("/{postId}/average")
    public ResponseEntity<?> getAverageRating(@PathVariable Long postId) {
        double averageRating = ratingsService.getAverageRating(postId);
        return ResponseEntity.ok(averageRating);
    }

    @PostMapping
    public ResponseEntity<?> submitRating(@RequestBody RatingsDTO ratingDTO) {
        try {
            RatingsDTO ratingsDTO = ratingsService.saveRating(ratingDTO);
            return ResponseEntity.ok(ratingsDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving rating");
        }
    }

}
