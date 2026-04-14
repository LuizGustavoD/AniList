package com.anilist.backend.server.DTO.response.auth;

public record AuthLoginResponseDTO(String token, String username, String role) {}
