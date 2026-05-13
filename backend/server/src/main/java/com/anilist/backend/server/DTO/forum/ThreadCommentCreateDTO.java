package com.anilist.backend.server.DTO.forum;

import jakarta.validation.constraints.NotBlank;

public record ThreadCommentCreateDTO(
    @NotBlank String content
) {}
