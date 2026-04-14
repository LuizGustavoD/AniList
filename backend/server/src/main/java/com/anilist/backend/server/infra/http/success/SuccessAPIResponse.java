package com.anilist.backend.server.infra.http.success;




public class SuccessAPIResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private java.util.Date timestamp;

    public SuccessAPIResponse(T data, String message) {
        this.success = true;
        this.message = message;
        this.data = data;
        this.timestamp = new java.util.Date();
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public java.util.Date getTimestamp() {
        return timestamp;
    }
}
