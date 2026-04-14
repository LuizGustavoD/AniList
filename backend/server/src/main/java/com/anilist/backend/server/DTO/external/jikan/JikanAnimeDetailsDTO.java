package com.anilist.backend.server.DTO.external.jikan;

import java.util.List;

public record JikanAnimeDetailsDTO(
    long mal_id,
    String title,
    String title_japanese,
    String title_english,
    String synopsis,
    Integer episodes,
    String duration,
    String status,
    JikanAiredDTO aired,
    Double score,
    Integer rank,
    Integer popularity,
    List<JikanGenreDTO> genres,
    List<JikanStudioDTO> studios,
    JikanTrailerDTO trailer
) {}
