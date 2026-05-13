package com.anilist.backend.server.controller.forum;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anilist.backend.server.DTO.forum.ThreadCommentCreateDTO;
import com.anilist.backend.server.DTO.forum.ThreadCreateDTO;
import com.anilist.backend.server.DTO.forum.ThreadResponseDTO;
import com.anilist.backend.server.infra.http.success.SuccessAPIResponse;
import com.anilist.backend.server.service.forum.ForumService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Fórum", description = "Tópicos de discussão e comentários")
@RestController
@RequestMapping("/api/forum")
@RequiredArgsConstructor
public class ForumController {

    private final ForumService forumService;

    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "userProfileLimiter")
    @Operation(summary = "Criar tópico")
    @PostMapping("/thread")
    public ResponseEntity<?> createThread(JwtAuthenticationToken authentication, @Valid @RequestBody ThreadCreateDTO request) {
        String username = authentication.getToken().getSubject();
        SuccessAPIResponse<Void> response = forumService.createThread(username, request);
        return ResponseEntity.ok(response);
    }

    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "userProfileLimiter")
    @Operation(summary = "Adicionar comentário")
    @PostMapping("/thread/{threadId}/comment")
    public ResponseEntity<?> addComment(JwtAuthenticationToken authentication, @PathVariable Long threadId, @Valid @RequestBody ThreadCommentCreateDTO request) {
        String username = authentication.getToken().getSubject();
        SuccessAPIResponse<Void> response = forumService.addComment(username, threadId, request);
        return ResponseEntity.ok(response);
    }

    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "animeControllerLimiter")
    @Operation(summary = "Listar tópicos")
    @GetMapping("/threads")
    public ResponseEntity<?> getThreads(@RequestParam(required = false) Long animeId, Pageable pageable) {
        Page<ThreadResponseDTO> threads = forumService.getThreads(animeId, pageable);
        return ResponseEntity.ok(new SuccessAPIResponse<>(threads, "Tópicos listados com sucesso"));
    }

    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "animeControllerLimiter")
    @Operation(summary = "Obter detalhes de um tópico")
    @GetMapping("/thread/{threadId}")
    public ResponseEntity<?> getThread(@PathVariable Long threadId) {
        ThreadResponseDTO thread = forumService.getThread(threadId);
        return ResponseEntity.ok(new SuccessAPIResponse<>(thread, "Detalhes do tópico"));
    }
}
