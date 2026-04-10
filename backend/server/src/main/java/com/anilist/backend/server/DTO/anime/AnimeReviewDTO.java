package com.anilist.backend.server.DTO.anime;



import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record AnimeReviewDTO(
    @NotBlank(message = "Username cannot be blank")
    String username,

    @NotBlank(message = "Anime ID cannot be blank")
    String animeId,

    @NotBlank(message = "Review text cannot be blank")
    String reviewText,

    @NotBlank(message = "Rating cannot be blank")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    int rating
) {
    
}
