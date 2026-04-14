package com.anilist.backend.server.DTO.external.jikan;

public record JikanSeasonAnimeDTO(
    long mal_id,
    String title,
    String season,
    Integer year,
    Integer episodes,
    Double score
) {}
