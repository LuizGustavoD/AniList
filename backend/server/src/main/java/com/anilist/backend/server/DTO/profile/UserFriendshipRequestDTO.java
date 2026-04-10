package com.anilist.backend.server.DTO.profile;

import jakarta.validation.constraints.NotBlank;

public record UserFriendshipRequestDTO(
    @NotBlank(message = "Sender username is required")
    String senderUsername,

    @NotBlank(message = "Receiver username is required")
    String receiverUsername
) {
    
}
