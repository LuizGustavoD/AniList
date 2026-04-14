package com.anilist.backend.server.DTO.response.anime;

import java.util.List;

public record AnimeSearchResponseDTO(List<AnimeResponseDTO> results, int totalCount) {}
