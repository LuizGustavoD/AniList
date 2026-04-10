package com.anilist.backend.server.controller.profile;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anilist.backend.server.DTO.profile.UserFriendshipRequestDTO;
import com.anilist.backend.server.service.profile.UserFriendshipService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/profile/friendship")
public class UsersFriendshipController {

    private final UserFriendshipService userFriendshipService;
    
    @PostMapping("/send/request")
    public ResponseEntity<String> sendFriendRequest(@RequestBody UserFriendshipRequestDTO request) {
        String response = userFriendshipService.userFriendshipRequestSend(request);
        return ResponseEntity.ok(response);
    }
    
}
