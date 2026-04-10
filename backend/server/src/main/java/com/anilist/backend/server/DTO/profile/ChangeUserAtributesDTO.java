package com.anilist.backend.server.DTO.profile;

public record ChangeUserAtributesDTO(
    String username,
    String email,
    String about,
    String profilePictureUrl
) {
    
}
