package com.anilist.backend.server.DTO.response.anime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta de favoritar/desfavoritar anime")
public record AnimeFavoriteResponseDTO(
	@Schema(description = "ID do usuário", example = "1") Long userId,
	@Schema(description = "ID do anime", example = "10") Long animeId,
	@Schema(description = "Se está favoritado", example = "true") boolean isFavorite
) {}
