package com.anilist.backend.server.DTO.external.jikan;

public record JikanImageFormatDTO(
    String image_url,
    String small_image_url,
    String large_image_url
) {}
