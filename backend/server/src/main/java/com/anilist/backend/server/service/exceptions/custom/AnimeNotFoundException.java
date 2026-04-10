package com.anilist.backend.server.service.exceptions.custom;

public class AnimeNotFoundException extends RuntimeException {
    public AnimeNotFoundException(String message) {
        super(message);
    }
}
