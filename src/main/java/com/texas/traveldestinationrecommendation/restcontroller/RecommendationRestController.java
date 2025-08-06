package com.texas.traveldestinationrecommendation.restcontroller;

import com.texas.traveldestinationrecommendation.algorithm.RecommendationService;
import com.texas.traveldestinationrecommendation.dto.DestinationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/destination")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class RecommendationRestController {


    private final RecommendationService recommendationService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<DestinationDto>> getRecommendations(@PathVariable Long userId) {
        List<DestinationDto> recommendations = recommendationService.getRecommendationsForUser(userId);
        return ResponseEntity.ok(recommendations);
    }
}
