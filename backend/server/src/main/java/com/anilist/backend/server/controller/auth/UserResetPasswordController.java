package com.anilist.backend.server.controller.auth;

import java.util.List;

import org.springframework.http.ResponseEntity;
import com.anilist.backend.server.infra.http.success.SuccessAPIResponse;
import com.anilist.backend.server.infra.http.error.ErrorAPIResponse;
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

    @PostMapping("/confirm")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody UserForgotPasswordDTO request, @RequestParam String token) {
        try {
            String message = userForgotPasswordService.resetPassword(request, token);
            return ResponseEntity.ok(new SuccessAPIResponse<>(null, message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorAPIResponse<>(List.of(e.getMessage()), "Erro ao redefinir senha"));
        }
    }

    @PostMapping("/request")
    public ResponseEntity<?> sendForgotPasswordEmail(@Valid @RequestBody UserForgotPasswordVerifyAccountDTO request) {
        try {
            String message = userForgotPasswordService.sendForgotRequest(request);
            return ResponseEntity.ok(new SuccessAPIResponse<>(null, message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorAPIResponse<>(List.of(e.getMessage()), "Erro ao enviar email de recuperação"));
        }
    }

}
