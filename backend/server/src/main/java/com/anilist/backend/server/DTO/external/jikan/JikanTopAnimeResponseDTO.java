package com.anilist.backend.server.DTO.external.jikan;

import java.util.List;

public record JikanTopAnimeResponseDTO(
    List<JikanTopAnimeDTO> data,
    JikanPaginationDTO pagination
) {}
