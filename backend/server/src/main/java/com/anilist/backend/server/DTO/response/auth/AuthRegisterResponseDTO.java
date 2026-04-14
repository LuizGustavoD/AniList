package com.anilist.backend.server.DTO.response.auth;

import java.util.UUID;

public record AuthRegisterResponseDTO(UUID userId, String username, String email) {}
