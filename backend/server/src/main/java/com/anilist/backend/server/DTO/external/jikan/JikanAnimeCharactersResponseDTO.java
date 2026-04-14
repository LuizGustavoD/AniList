package com.anilist.backend.server.DTO.external.jikan;

import java.util.List;

public record JikanAnimeCharactersResponseDTO(
    List<JikanAnimeCharacterDTO> data
) {}
