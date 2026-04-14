package com.anilist.backend.server.DTO.admin.relationship;

public record AdminReviewUpdateDTO(
    Integer rating,
    String comment
) {}
