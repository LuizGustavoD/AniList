package com.anilist.backend.server.DTO.external.jikan;

public record JikanVoiceActorDTO(
    JikanPersonDTO person,
    String language
) {}
