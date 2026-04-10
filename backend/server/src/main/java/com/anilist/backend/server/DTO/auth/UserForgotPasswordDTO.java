package com.anilist.backend.server.DTO.auth;

import jakarta.validation.constraints.NotBlank;

public record UserForgotPasswordDTO(

    @NotBlank(message = "New password is required")
    String newPassword
) {
    
}
