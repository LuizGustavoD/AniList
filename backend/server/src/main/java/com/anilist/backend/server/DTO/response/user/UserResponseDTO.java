package com.anilist.backend.server.DTO.response.user;

import java.util.UUID;

public record UserResponseDTO(UUID id, String username, String email) {}