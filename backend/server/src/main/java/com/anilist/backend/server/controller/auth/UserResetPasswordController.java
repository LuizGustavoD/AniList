package com.anilist.backend.server.controller.auth;

import java.util.List;

import org.springframework.http.ResponseEntity;
import com.anilist.backend.server.infra.http.success.SuccessAPIResponse;
import com.anilist.backend.server.controller.exceptions.StandartError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anilist.backend.server.DTO.auth.UserForgotPasswordDTO;
import com.anilist.backend.server.DTO.auth.UserForgotPasswordVerifyAccountDTO;
import com.anilist.backend.server.service.auth.UserForgotPasswordService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/reset-password")
public class UserResetPasswordController {
    
    private final UserForgotPasswordService userForgotPasswordService;

    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "userResetPasswordLimiter")
    @PostMapping("/confirm")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody UserForgotPasswordDTO request, @RequestParam String token, jakarta.servlet.http.HttpServletRequest httpRequest) {
        try {
            String message = userForgotPasswordService.resetPassword(request, token);
            return ResponseEntity.ok(new SuccessAPIResponse<>(null, message));
        } catch (Exception e) {
            StandartError error = new StandartError();
            error.setTimestamp(java.time.Instant.now());
            error.setStatus(400);
            error.setError("Bad Request");
            error.setMessage("Erro ao redefinir senha");
            error.setPath(httpRequest.getRequestURI());
            error.setErrors(List.of(e.getMessage()));
            return ResponseEntity.badRequest().body(error);
        }
    }

    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "userResetPasswordLimiter")
    @PostMapping("/request")
    public ResponseEntity<?> sendForgotPasswordEmail(@Valid @RequestBody UserForgotPasswordVerifyAccountDTO request, jakarta.servlet.http.HttpServletRequest httpRequest) {
        try {
            String message = userForgotPasswordService.sendForgotRequest(request);
            return ResponseEntity.ok(new SuccessAPIResponse<>(null, message));
        } catch (Exception e) {
            StandartError error = new StandartError();
            error.setTimestamp(java.time.Instant.now());
            error.setStatus(400);
            error.setError("Bad Request");
            error.setMessage("Erro ao enviar email de recuperação");
            error.setPath(httpRequest.getRequestURI());
            error.setErrors(List.of(e.getMessage()));
            return ResponseEntity.badRequest().body(error);
        }
    }

}
