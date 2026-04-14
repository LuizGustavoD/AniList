package com.anilist.backend.server.DTO.external.jikan;

public record JikanTopAnimeDTO(
    long mal_id,
    String title,
    Double score,
    Integer rank,
    Integer popularity
) {}
