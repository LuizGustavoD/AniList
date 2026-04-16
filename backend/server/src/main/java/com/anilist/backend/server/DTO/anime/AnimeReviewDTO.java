package com.anilist.backend.server.DTO.anime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para adicionar review ao anime")
public record AnimeReviewDTO(
    @Schema(description = "Nome do usuário", example = "user123")
    @NotBlank(message = "Username cannot be blank")
    String username,

    @Schema(description = "ID do anime", example = "10")
    @NotBlank(message = "Anime ID cannot be blank")
    String animeId,

    @Schema(description = "Texto da review", example = "Ótimo anime!")
    @NotBlank(message = "Review text cannot be blank")
    String reviewText,

    @Schema(description = "Nota da review", example = "5")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    int rating
) {}
