package com.anilist.backend.server.DTO.message;

import java.time.LocalDateTime;

public record MessageResponseDTO(
    Long id,
    Long senderId,
    String content,
    LocalDateTime sentAt,
    boolean isGroup,
    Long receiverId, // for direct
    Long groupId     // for group
) {}
