package com.anilist.backend.server.controller.auth;

import org.springframework.http.ResponseEntity;
import com.anilist.backend.server.infra.http.success.SuccessAPIResponse;
import com.anilist.backend.server.controller.exceptions.StandartError;
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
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO, jakarta.servlet.http.HttpServletRequest request) {
        try {
            String message = userRegisterService.registerUser(userRegisterDTO);
            return ResponseEntity.ok(new SuccessAPIResponse<>(null, message));
        } catch (Exception e) {
            StandartError error = new StandartError();
            error.setTimestamp(java.time.Instant.now());
            error.setStatus(400);
            error.setError("Bad Request");
            error.setMessage("Erro ao registrar usuário");
            error.setPath(request.getRequestURI());
            error.setErrors(List.of(e.getMessage()));
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/confirm-email")
    public ResponseEntity<?> confirmEmail(@RequestParam String token, jakarta.servlet.http.HttpServletRequest request) {
        try {
            String authToken = userRegisterService.confirmEmail(token);
            return ResponseEntity.ok(new SuccessAPIResponse<>(authToken, "Email confirmado com sucesso"));
        } catch (Exception e) {
            StandartError error = new StandartError();
            error.setTimestamp(java.time.Instant.now());
            error.setStatus(400);
            error.setError("Bad Request");
            error.setMessage("Erro ao confirmar email");
            error.setPath(request.getRequestURI());
            error.setErrors(List.of(e.getMessage()));
            return ResponseEntity.badRequest().body(error);
        }
    }
}
