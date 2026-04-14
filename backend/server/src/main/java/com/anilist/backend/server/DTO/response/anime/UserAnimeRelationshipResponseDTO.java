package com.anilist.backend.server.DTO.response.anime;

public record UserAnimeRelationshipResponseDTO(Long id, Long userId, Long animeId, String status, Integer score, String notes) {}
