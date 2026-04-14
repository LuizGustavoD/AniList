package com.anilist.backend.server.DTO.response.user;

import java.util.List;
import com.anilist.backend.server.DTO.response.profile.UserFriendshipResponseDTO;

public record UserFriendListResponseDTO(List<UserFriendshipResponseDTO> friends) {}
