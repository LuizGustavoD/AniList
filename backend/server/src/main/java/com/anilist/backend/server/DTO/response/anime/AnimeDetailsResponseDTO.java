package com.anilist.backend.server.DTO.response.anime;

public record AnimeDetailsResponseDTO(Long id, String title, String synopsis, String imageUrl, Integer episodes, Double score) {}
