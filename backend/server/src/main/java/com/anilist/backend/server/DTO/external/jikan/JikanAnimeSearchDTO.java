package com.anilist.backend.server.DTO.external.jikan;

import java.util.List;

public record JikanAnimeSearchDTO(
    long mal_id,
    String url,
    JikanImagesDTO images,
    String title,
    String title_english,
    Integer episodes,
    String status,
    Double score,
    List<JikanGenreDTO> genres
) {}
