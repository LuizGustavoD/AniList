package com.anilist.backend.server.controller.moderation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anilist.backend.server.DTO.moderation.ReportRequestDTO;
import com.anilist.backend.server.DTO.moderation.UserBlockRequestDTO;
import com.anilist.backend.server.infra.http.success.SuccessAPIResponse;
import com.anilist.backend.server.service.moderation.ModerationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Moderação", description = "Bloqueio de usuários e envio de denúncias")
@RestController
@RequestMapping("/api/moderation")
@RequiredArgsConstructor
public class ModerationController {

    private final ModerationService moderationService;

    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "userProfileLimiter")
    @Operation(summary = "Bloquear usuário")
    @PostMapping("/block")
    public ResponseEntity<?> blockUser(JwtAuthenticationToken authentication, @Valid @RequestBody UserBlockRequestDTO request) {
        String username = authentication.getToken().getSubject();
        SuccessAPIResponse<Void> response = moderationService.blockUser(username, request);
        return ResponseEntity.ok(response);
    }

    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "userProfileLimiter")
    @Operation(summary = "Desbloquear usuário")
    @DeleteMapping("/block/{blockedUsername}")
    public ResponseEntity<?> unblockUser(JwtAuthenticationToken authentication, @PathVariable String blockedUsername) {
        String username = authentication.getToken().getSubject();
        SuccessAPIResponse<Void> response = moderationService.unblockUser(username, blockedUsername);
        return ResponseEntity.ok(response);
    }

    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "userProfileLimiter")
    @Operation(summary = "Enviar denúncia")
    @PostMapping("/report")
    public ResponseEntity<?> submitReport(JwtAuthenticationToken authentication, @Valid @RequestBody ReportRequestDTO request) {
        String username = authentication.getToken().getSubject();
        SuccessAPIResponse<Void> response = moderationService.submitReport(username, request);
        return ResponseEntity.ok(response);
    }
}
