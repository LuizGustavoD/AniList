package com.anilist.backend.server.DTO.external.jikan;

import java.util.List;

public record JikanSeasonAnimeResponseDTO(
    List<JikanSeasonAnimeDTO> data,
    JikanPaginationDTO pagination
) {}
