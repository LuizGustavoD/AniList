package com.anilist.backend.server.DTO.anime;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para favoritar/desfavoritar anime")
public record UserAnimeFavoriteDTO(
    @Schema(description = "Nome do usuário", example = "user123")
    @NotBlank(message = "Username cannot be blank")
    String username,

    @Schema(description = "ID do anime", example = "10")
    @NotBlank(message = "Anime ID cannot be blank")
    String animeId,

    @Schema(description = "Favoritar ou não", example = "true")
    boolean favorite
) {}
