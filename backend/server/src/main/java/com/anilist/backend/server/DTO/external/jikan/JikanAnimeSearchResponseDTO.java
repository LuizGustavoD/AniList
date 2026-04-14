package com.anilist.backend.server.DTO.external.jikan;

import java.util.List;

public record JikanAnimeSearchResponseDTO(
    List<JikanAnimeSearchDTO> data,
    JikanPaginationDTO pagination
) {}
