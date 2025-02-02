package com.example.mimir.exceptions;

import org.springframework.http.HttpStatus;

public abstract class AuthorizationException extends HttpClientException {
    public static final HttpStatus DEFAULT_HTTP_STATUS = HttpStatus.UNAUTHORIZED;
    public static final String DEFAULT_HTTP_PATH = "AUTHORIZATION_EXCEPTION";

    public AuthorizationException(String message, String httpCode) {
        super(message, DEFAULT_HTTP_STATUS, String.join("-", DEFAULT_HTTP_PATH, httpCode));
    }
}
