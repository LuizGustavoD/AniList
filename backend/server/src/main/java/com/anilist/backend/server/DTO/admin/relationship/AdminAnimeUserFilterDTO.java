package com.anilist.backend.server.DTO.admin.relationship;

import com.anilist.backend.server.models.anime.EnumAnimeStatus;

public record AdminAnimeUserFilterDTO(
    String userId,
    EnumAnimeStatus status,
    Boolean isFavorite
) {}
