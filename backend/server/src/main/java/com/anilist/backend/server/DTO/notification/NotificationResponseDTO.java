package com.anilist.backend.server.DTO.notification;

import java.time.Instant;

import com.anilist.backend.server.models.notification.EnumNotificationType;
import com.anilist.backend.server.models.notification.NotificationModel;

public record NotificationResponseDTO(
        Long id,
        EnumNotificationType type,
        String content,
        boolean isRead,
        Instant createdAt
) {
    public static NotificationResponseDTO fromEntity(NotificationModel notification) {
        return new NotificationResponseDTO(
                notification.getId(),
                notification.getType(),
                notification.getContent(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}
