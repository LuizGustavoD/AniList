package com.anilist.backend.server.controller.auth;

import org.springframework.http.ResponseEntity;
import com.anilist.backend.server.infra.http.success.SuccessAPIResponse;
import com.anilist.backend.server.infra.http.error.ErrorAPIResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anilist.backend.server.DTO.auth.UserRegisterDTO;
import com.anilist.backend.server.service.auth.UserRegisterService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/register")
public class UserRegisterController {
    
    private final UserRegisterService userRegisterService;

    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        try {
            String message = userRegisterService.registerUser(userRegisterDTO);
            return ResponseEntity.ok(new SuccessAPIResponse<>(null, message));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorAPIResponse<>(List.of(e.getMessage()), "Erro ao registrar usuário"));
        }
    }

    @PostMapping("/confirm-email")
    public ResponseEntity<?> confirmEmail(@RequestParam String token) {
        try {
            String authToken = userRegisterService.confirmEmail(token);
            return ResponseEntity.ok(new SuccessAPIResponse<>(authToken, "Email confirmado com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorAPIResponse<>(List.of(e.getMessage()), "Erro ao confirmar email"));
        }
    }
}
