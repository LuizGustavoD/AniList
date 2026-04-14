package com.anilist.backend.server.DTO.admin.relationship;

import com.anilist.backend.server.models.anime.EnumAnimeStatus;

public record AdminUserAnimeUpdateDTO(
    EnumAnimeStatus status,
    boolean isFavorite
) {}
