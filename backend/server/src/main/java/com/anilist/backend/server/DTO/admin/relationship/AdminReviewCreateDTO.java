package com.anilist.backend.server.DTO.admin.relationship;

public record AdminReviewCreateDTO(
    String userId,
    Long animeId,
    Integer rating,
    String comment
) {}

