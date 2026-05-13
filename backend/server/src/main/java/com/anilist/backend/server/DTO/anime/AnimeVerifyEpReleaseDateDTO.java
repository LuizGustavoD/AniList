package com.anilist.backend.server.DTO.anime;


import jakarta.validation.constraints.NotBlank;

public record AnimeVerifyEpReleaseDateDTO(
    
    @NotBlank(message = "Title is required")
    String animeTitle
) { 
}
