package com.anilist.backend.server.controller.exceptions;

import java.time.Instant;
import java.util.List;

import lombok.Data;

@Data
public class StandartError {
    private Instant timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
    private List<String> errors;
}
