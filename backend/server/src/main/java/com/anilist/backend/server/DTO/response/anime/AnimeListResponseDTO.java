package com.anilist.backend.server.DTO.response.anime;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Lista de animes retornada na busca")
public record AnimeListResponseDTO(
	@Schema(description = "Lista de animes") List<AnimeResponseDTO> animes
) {}
