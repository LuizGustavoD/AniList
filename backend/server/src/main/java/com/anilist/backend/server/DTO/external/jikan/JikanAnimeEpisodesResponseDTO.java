package com.anilist.backend.server.DTO.external.jikan;

import java.util.List;

public record JikanAnimeEpisodesResponseDTO(
    List<JikanAnimeEpisodeDTO> data,
    JikanPaginationDTO pagination
) {}
