package com.anilist.backend.server.DTO.admin.anime;

import java.time.LocalDate;

public record AdminAnimeCreateDTO(
    Long malId,
    String title,
    String description,
    String imageUrl,
    LocalDate releaseDate
) {}

