package org.iclass.board.controller;

import lombok.RequiredArgsConstructor;
import org.iclass.board.dto.RatingsDTO;
import org.iclass.board.service.RatingsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ratings")
public class ApiRatingsController {

    private final RatingsService ratingsService;

    @PostMapping
    public ResponseEntity<?> submitRating(@RequestBody RatingsDTO ratingDTO) {
        try {
            ratingsService.saveRating(ratingDTO);
            return ResponseEntity.ok("Rating saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving rating");
        }
    }

}
