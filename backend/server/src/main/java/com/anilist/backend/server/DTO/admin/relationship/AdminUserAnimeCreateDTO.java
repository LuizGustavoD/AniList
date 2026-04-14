package com.anilist.backend.server.DTO.admin.relationship;

import com.anilist.backend.server.models.anime.EnumAnimeStatus;

public record AdminUserAnimeCreateDTO(
    String userId,
    Long animeId,
    EnumAnimeStatus status,
    boolean isFavorite
) {}
