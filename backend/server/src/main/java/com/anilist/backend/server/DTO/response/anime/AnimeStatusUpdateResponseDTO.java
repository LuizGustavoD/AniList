package com.anilist.backend.server.DTO.response.anime;

import java.util.UUID;

import com.anilist.backend.server.models.anime.EnumAnimeStatus;


public record AnimeStatusUpdateResponseDTO(UUID userId, Long animeId, String status, String message) {

    public AnimeStatusUpdateResponseDTO(UUID userId, Long animeId, EnumAnimeStatus status, String message) {
        this(userId, animeId, status.name(), message);
    }

    public AnimeStatusUpdateResponseDTO(UUID userId, Long animeId, EnumAnimeStatus status) {
        this(userId, animeId, status.name(), "Anime status updated successfully.");
    }
}
