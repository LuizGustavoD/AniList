package com.anilist.backend.server.DTO.response.anime;

public record AnimeFavoriteResponseDTO(Long userId, Long animeId, boolean isFavorite) {}
