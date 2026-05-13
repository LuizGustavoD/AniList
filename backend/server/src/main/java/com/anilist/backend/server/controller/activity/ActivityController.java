package com.anilist.backend.server.controller.activity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anilist.backend.server.DTO.activity.ActivityResponseDTO;
import com.anilist.backend.server.infra.http.success.SuccessAPIResponse;
import com.anilist.backend.server.service.activity.ActivityService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Feed de Atividades", description = "Feed de atividades de amigos")
@RestController
@RequestMapping("/api/feed")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "userProfileLimiter")
    @Operation(summary = "Obter Feed", description = "Retorna atividades dos amigos do usuário autenticado")
    @GetMapping
    public ResponseEntity<?> getFeed(JwtAuthenticationToken authentication, Pageable pageable) {
        String username = authentication.getToken().getSubject();
        Page<ActivityResponseDTO> feed = activityService.getFeed(username, pageable);
        return ResponseEntity.ok(new SuccessAPIResponse<>(feed, "Feed carregado com sucesso"));
    }
}
