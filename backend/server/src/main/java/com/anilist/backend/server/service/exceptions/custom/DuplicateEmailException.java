package com.anilist.backend.server.service.exceptions.custom;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String message) {
        super(message);
    }
}
