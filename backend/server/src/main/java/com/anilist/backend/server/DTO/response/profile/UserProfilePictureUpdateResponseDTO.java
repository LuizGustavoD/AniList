package com.anilist.backend.server.DTO.response.profile;

import java.util.UUID;

public record UserProfilePictureUpdateResponseDTO(UUID userId, String avatarUrl, String message) {}
