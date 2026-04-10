package com.anilist.backend.server.DTO.anime;

import com.anilist.backend.server.models.anime.EnumAnimeStatus;

import jakarta.validation.constraints.NotBlank;

public record UserAnimeAddDTO(

    @NotBlank(message = "Username cannot be blank")
    String username,

    @NotBlank(message = "Anime ID cannot be blank") 
    String animeId,

    @NotBlank(message = "Status cannot be blank")
    EnumAnimeStatus status
) {
    
}
