package com.example.mimir.exceptions;

import org.springframework.http.HttpStatus;

public abstract class HttpClientException extends AppException {
    private final HttpStatus httpStatus;
    protected final String httpPath;

    public HttpClientException (String message, HttpStatus httpStatus, String httpPath) {
        super(message);
        this.httpStatus = httpStatus;
        this.httpPath = httpPath;
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }

    public String getHttpPath() {
        return httpPath;
    }
}
