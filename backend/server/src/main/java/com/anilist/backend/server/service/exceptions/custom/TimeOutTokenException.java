package com.anilist.backend.server.service.exceptions.custom;

public class TimeOutTokenException extends RuntimeException {
    public TimeOutTokenException(String message) {
        super(message);
    }
    
}
