package com.anilist.backend.server.controller.recommendation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anilist.backend.server.infra.http.success.SuccessAPIResponse;
import com.anilist.backend.server.service.recommendation.RecommendationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Recomendações", description = "Recomendações de animes personalizadas")
@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "animeControllerLimiter")
    @Operation(summary = "Obter Recomendações")
    @GetMapping
    public ResponseEntity<?> getRecommendations(JwtAuthenticationToken authentication) {
        String username = authentication.getToken().getSubject();
        var recommendations = recommendationService.getRecommendations(username);
        return ResponseEntity.ok(new SuccessAPIResponse<>(recommendations, "Recomendações geradas com sucesso"));
    }
}
