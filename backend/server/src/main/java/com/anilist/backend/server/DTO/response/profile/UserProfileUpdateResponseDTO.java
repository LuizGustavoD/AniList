package com.anilist.backend.server.DTO.response.profile;

import java.util.UUID;

public record UserProfileUpdateResponseDTO(UUID id, String username, String email, String avatarUrl, String about, String message) {}
