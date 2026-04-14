package com.anilist.backend.server.DTO.external.jikan;

public record JikanAnimeEpisodeDTO(
    long mal_id,
    String title,
    String title_japanese,
    String aired,
    Double score,
    Boolean filler,
    Boolean recap
) {}
