package com.anilist.backend.server.DTO.response.anime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informações básicas de um anime")
public record AnimeResponseDTO(
	@Schema(description = "ID do anime", example = "1") Long id,
	@Schema(description = "Título do anime", example = "Naruto") String title,
	@Schema(description = "URL da imagem do anime", example = "https://cdn.../naruto.jpg") String imageUrl
) {}