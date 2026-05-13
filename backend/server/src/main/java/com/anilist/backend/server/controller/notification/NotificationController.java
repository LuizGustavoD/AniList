package com.anilist.backend.server.controller.notification;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anilist.backend.server.DTO.notification.NotificationResponseDTO;
import com.anilist.backend.server.infra.http.success.SuccessAPIResponse;
import com.anilist.backend.server.service.notification.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Notificações", description = "Gerenciamento de notificações do usuário")
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "userProfileLimiter")
    @Operation(summary = "Listar notificações")
    @GetMapping
    public ResponseEntity<?> getNotifications(JwtAuthenticationToken authentication) {
        String username = authentication.getToken().getSubject();
        List<NotificationResponseDTO> notifications = notificationService.getUserNotifications(username);
        return ResponseEntity.ok(new SuccessAPIResponse<>(notifications, "Notificações carregadas com sucesso"));
    }

    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "userProfileLimiter")
    @Operation(summary = "Marcar notificação como lida")
    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id, JwtAuthenticationToken authentication) {
        String username = authentication.getToken().getSubject();
        SuccessAPIResponse<Void> response = notificationService.markAsRead(username, id);
        return ResponseEntity.ok(response);
    }

    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "userProfileLimiter")
    @Operation(summary = "Marcar todas as notificações como lidas")
    @PutMapping("/read/all")
    public ResponseEntity<?> markAllAsRead(JwtAuthenticationToken authentication) {
        String username = authentication.getToken().getSubject();
        SuccessAPIResponse<Void> response = notificationService.markAllAsRead(username);
        return ResponseEntity.ok(response);
    }

}
