package com.anilist.backend.server.DTO.message;

import java.time.LocalDateTime;
import java.util.UUID;

public record DirectMessageDTO(
    UUID senderId,
    UUID receiverId,
    String content,
    LocalDateTime sentAt
) {}
