package com.anilist.backend.server.DTO.response.anime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de review de anime")
public record AnimeReviewResponseDTO(
	@Schema(description = "ID da review", example = "1") Long id,
	@Schema(description = "ID do anime", example = "10") Long animeId,
	@Schema(description = "Nome do usuário", example = "user123") String username,
	@Schema(description = "Texto da review", example = "Ótimo anime!") String review,
	@Schema(description = "Nota da review", example = "5") Integer rating
) {}
