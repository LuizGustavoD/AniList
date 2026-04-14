package com.anilist.backend.server.controller.auth;

import org.springframework.http.ResponseEntity;
import com.anilist.backend.server.infra.http.success.SuccessAPIResponse;
import com.anilist.backend.server.infra.http.error.ErrorAPIResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anilist.backend.server.DTO.auth.UserLoginDTO;
import com.anilist.backend.server.service.auth.UserLoginService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/login")
public class UserLoginController {
    
    private final UserLoginService userLoginService;

    @PostMapping
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String token = userLoginService.login(userLoginDTO);
            return ResponseEntity.ok(new SuccessAPIResponse<>(token, "Login realizado com sucesso"));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(403).body(new ErrorAPIResponse<>(List.of(e.getMessage()), "Usuário ou senha inválidos"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(new ErrorAPIResponse<>(List.of(e.getMessage()), "Usuário não encontrado"));
        } catch (org.springframework.security.core.AuthenticationException e) {
            return ResponseEntity.status(401).body(new ErrorAPIResponse<>(List.of("Usuário ou senha inválidos"), "Falha de autenticação"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorAPIResponse<>(List.of(e.getMessage()), "Erro interno ao autenticar"));
        }
    }
}
