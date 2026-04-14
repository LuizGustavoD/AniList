package com.anilist.backend.server.infra.http.success;

import com.anilist.backend.server.infra.http.DefaultAPIResponse;

public class SuccessAPIResponse<T> extends DefaultAPIResponse<T> {

    public SuccessAPIResponse(T data, String message) {
        Success = true;
        Message = message;
        Data = data;
        Timestamp = new java.sql.Date(System.currentTimeMillis());
    }
}
