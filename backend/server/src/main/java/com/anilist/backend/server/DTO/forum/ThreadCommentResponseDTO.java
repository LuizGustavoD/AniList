package com.anilist.backend.server.DTO.forum;

import java.time.Instant;

public record ThreadCommentResponseDTO(
    Long id,
    String authorName,
    String authorProfilePicture,
    String content,
    Instant createdAt
) {}
