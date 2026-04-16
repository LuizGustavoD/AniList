package com.anilist.backend.server.controller.auth;

import org.springframework.http.ResponseEntity;
import com.anilist.backend.server.infra.http.success.SuccessAPIResponse;
import com.anilist.backend.server.controller.exceptions.StandartError;
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

    @io.github.resilience4j.ratelimiter.annotation.RateLimiter(name = "userLoginLimiter")
    @PostMapping
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDTO userLoginDTO, jakarta.servlet.http.HttpServletRequest request) {
        try {
            String token = userLoginService.login(userLoginDTO);
            return ResponseEntity.ok(new SuccessAPIResponse<>(token, "Login realizado com sucesso"));
        } catch (IllegalStateException e) {
            StandartError error = new StandartError();
            error.setTimestamp(java.time.Instant.now());
            error.setStatus(403);
            error.setError("Forbidden");
            error.setMessage("Usuário ou senha inválidos");
            error.setPath(request.getRequestURI());
            error.setErrors(List.of(e.getMessage()));
            return ResponseEntity.status(403).body(error);
        } catch (IllegalArgumentException e) {
            StandartError error = new StandartError();
            error.setTimestamp(java.time.Instant.now());
            error.setStatus(404);
            error.setError("Not Found");
            error.setMessage("Usuário não encontrado");
            error.setPath(request.getRequestURI());
            error.setErrors(List.of(e.getMessage()));
            return ResponseEntity.status(404).body(error);
        } catch (org.springframework.security.core.AuthenticationException e) {
            StandartError error = new StandartError();
            error.setTimestamp(java.time.Instant.now());
            error.setStatus(401);
            error.setError("Unauthorized");
            error.setMessage("Falha de autenticação");
            error.setPath(request.getRequestURI());
            error.setErrors(List.of("Usuário ou senha inválidos"));
            return ResponseEntity.status(401).body(error);
        } catch (Exception e) {
            StandartError error = new StandartError();
            error.setTimestamp(java.time.Instant.now());
            error.setStatus(500);
            error.setError("Internal Server Error");
            error.setMessage("Erro interno ao autenticar");
            error.setPath(request.getRequestURI());
            error.setErrors(List.of(e.getMessage()));
            return ResponseEntity.status(500).body(error);
        }
    }
}
