package com.anilist.backend.server.service.exceptions.custom;

public class FriendshipRequestException extends RuntimeException {
    public FriendshipRequestException(String message) {
        super(message);
    }
}
