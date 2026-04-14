package com.anilist.backend.server.DTO.admin.user;

import com.anilist.backend.server.models.role.EnumRole;

public record AdminUserCreateDTO(
    String username,
    String password,
    String email,
    String about,
    String profilePicture,
    boolean isConfirmed,
    EnumRole role
) {}
