package com.anilist.backend.server.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anilist.backend.server.DTO.auth.UserRegisterDTO;
import com.anilist.backend.server.service.auth.UserRegisterService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/register")
public class UserRegisterController {
    
    private final UserRegisterService userRegisterService;

    @PostMapping
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        String message = userRegisterService.registerUser(userRegisterDTO);
        return ResponseEntity.ok(Map.of("message", message));
    }

    @PostMapping("/confirm-email")
    public ResponseEntity<Map<String, String>> confirmEmail(@RequestParam String token) {
        String authToken = userRegisterService.confirmEmail(token);
        return ResponseEntity.ok(Map.of("token", authToken));
    }
}
