package com.anilist.backend.server.DTO.admin.anime;

import java.time.LocalDate;

public record AdminAnimeUpdateDTO(
    String title,
    String description,
    String imageUrl,
    LocalDate releaseDate,
    Long malId
) {}

