package com.anilist.backend.server.DTO.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserForgotPasswordVerifyAccountDTO(

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email
) {
    
}
