package com.anilist.backend.server.DTO.admin.user;

public record AdminUserUpdateDTO(
    String username,
    String email,
    String about,
    String profilePicture,
    boolean isConfirmed
) {}
