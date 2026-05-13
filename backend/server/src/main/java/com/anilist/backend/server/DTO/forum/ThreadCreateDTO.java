package com.anilist.backend.server.DTO.forum;

import jakarta.validation.constraints.NotBlank;

public record ThreadCreateDTO(
    Long animeId,
    @NotBlank String title,
    @NotBlank String content
) {}
