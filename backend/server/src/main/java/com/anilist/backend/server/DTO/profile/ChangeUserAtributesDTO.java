package com.anilist.backend.server.DTO.profile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ChangeUserAtributesDTO(

    @NotBlank(message = "Username cannot be blank")
    String username,

    @Email(message = "Invalid email format")
    String email,

    @NotBlank(message = "About section cannot be blank")
    String about,

    @NotBlank(message = "Password cannot be blank")
    String password,

    String profilePictureUrl
) {
    
}
