package com.anilist.backend.server.service.exceptions.custom;

public class UserForbiddenAccessException extends RuntimeException {
    public UserForbiddenAccessException(String message) {
        super(message);
    }
}