package com.anilist.backend.server.DTO.external.jikan;

public record JikanPaginationDTO(
    Integer last_visible_page,
    Boolean has_next_page,
    Integer current_page
) {}
