package com.anilist.backend.server.infra.http.error;

import java.util.Date;
import java.util.List;

import com.anilist.backend.server.infra.http.DefaultAPIResponse;

public class ErrorAPIResponse<T> extends DefaultAPIResponse<T> {

    public ErrorAPIResponse(List<String> errors, String message) {
        Success = false;
        Message = message;
        Errors = errors;
        Timestamp = new java.sql.Date(System.currentTimeMillis());
    }
}
