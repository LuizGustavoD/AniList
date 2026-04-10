package com.anilist.backend.server.controller.auth;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> resetPassword(@Valid @RequestBody UserForgotPasswordDTO request, @RequestParam String token) {
        return ResponseEntity.ok(userForgotPasswordService.resetPassword(request, token));
    }

    @PostMapping("/request")
    public ResponseEntity<String> sendForgotPasswordEmail(@Valid @RequestBody UserForgotPasswordVerifyAccountDTO request) {
        return ResponseEntity.ok(userForgotPasswordService.sendForgotRequest(request));
    }

}
