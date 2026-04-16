package com.anilist.backend.server.DTO.anime;

import com.anilist.backend.server.models.anime.EnumAnimeStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para atualizar status do anime na lista do usuário")
public record UserAnimeStatusUpdateDTO(
    @Schema(description = "Nome do usuário", example = "user123")
    @NotBlank(message = "Username cannot be blank")
    String username,

    @Schema(description = "ID do anime", example = "10")
    @NotBlank(message = "Anime ID cannot be blank")
    String animeId,

    @Schema(description = "Status do anime", example = "WATCHING")
    @NotNull(message = "Status cannot be null")
    EnumAnimeStatus status
) {}
