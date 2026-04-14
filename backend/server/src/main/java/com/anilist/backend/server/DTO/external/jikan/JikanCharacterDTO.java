package com.anilist.backend.server.DTO.external.jikan;

public record JikanCharacterDTO(
    long mal_id,
    String name,
    JikanImagesDTO images
) {}
