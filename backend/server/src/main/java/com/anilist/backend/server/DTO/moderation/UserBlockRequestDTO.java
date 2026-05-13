package com.anilist.backend.server.DTO.moderation;

import jakarta.validation.constraints.NotBlank;

public record UserBlockRequestDTO(
    @NotBlank String blockedUsername
) {}
