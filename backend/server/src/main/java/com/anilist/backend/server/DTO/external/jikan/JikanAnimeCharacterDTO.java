package com.anilist.backend.server.DTO.external.jikan;

import java.util.List;

public record JikanAnimeCharacterDTO(
    JikanCharacterDTO character,
    String role,
    List<JikanVoiceActorDTO> voice_actors
) {}
