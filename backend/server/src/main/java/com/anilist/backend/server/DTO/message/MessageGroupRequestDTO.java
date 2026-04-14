package com.anilist.backend.server.DTO.message;

import java.time.LocalDateTime;
import java.util.List;

public record MessageGroupRequestDTO(
    Long groupId,
    String content,
    LocalDateTime sentAt,
    List<Long> mentionedUserIds
) {}
