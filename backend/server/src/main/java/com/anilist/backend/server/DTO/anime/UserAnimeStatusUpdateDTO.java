package com.anilist.backend.server.DTO.anime;

import com.anilist.backend.server.models.anime.EnumAnimeStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserAnimeStatusUpdateDTO(

    @NotBlank(message = "Username cannot be blank")
    String username,

    @NotBlank(message = "Anime ID cannot be blank")
    String animeId,

    @NotNull(message = "Status cannot be null")
    EnumAnimeStatus status
) {
}
