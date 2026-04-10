package com.anilist.backend.server.DTO.anime;

import jakarta.validation.constraints.NotBlank;

public record UserAnimeFavoriteDTO(

    @NotBlank(message = "Username cannot be blank")
    String username,

    @NotBlank(message = "Anime ID cannot be blank")
    String animeId,

    boolean favorite
) {
}
