package com.anilist.backend.server.DTO.response.anime;

public record AnimeReviewResponseDTO(Long id, Long animeId, String username, String review, Integer rating) {}
