package com.anilist.backend.server.DTO.response.profile;

import java.util.UUID;

public record UserFriendshipResponseDTO(UUID id, String username, String status) {}
