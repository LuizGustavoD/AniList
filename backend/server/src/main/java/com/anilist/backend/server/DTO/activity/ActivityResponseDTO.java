package com.anilist.backend.server.DTO.activity;

import java.time.Instant;

import com.anilist.backend.server.models.activity.ActivityModel;
import com.anilist.backend.server.models.activity.EnumActivityType;

public record ActivityResponseDTO(
        Long id,
        String username,
        String userProfilePicture,
        EnumActivityType activityType,
        String referenceId,
        Instant createdAt
) {
    public static ActivityResponseDTO fromEntity(ActivityModel activity) {
        return new ActivityResponseDTO(
                activity.getId(),
                activity.getUser().getUsername(),
                activity.getUser().getProfilePicture(),
                activity.getActivityType(),
                activity.getReferenceId(),
                activity.getCreatedAt()
        );
    }
}
