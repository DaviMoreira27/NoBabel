package com.example.mimir.exceptions;

public abstract class HttpClientException extends AppException {
    protected final int httpStatus;
    protected final String httpPath;

    public HttpClientException (String message, int httpStatus, String httpPath) {
        super(message);
        this.httpStatus = httpStatus;
        this.httpPath = httpPath;
    }
}
