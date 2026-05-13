package com.anilist.backend.server.DTO.forum;

import java.time.Instant;
import java.util.List;

public record ThreadResponseDTO(
    Long id,
    String authorName,
    String authorProfilePicture,
    Long animeId,
    String title,
    String content,
    Instant createdAt,
    List<ThreadCommentResponseDTO> comments
) {}
