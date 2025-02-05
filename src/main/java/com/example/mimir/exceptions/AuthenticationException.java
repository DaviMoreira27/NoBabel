package com.example.mimir.exceptions;

import org.springframework.http.HttpStatus;

public abstract class AuthenticationException extends HttpClientException {
    public static final HttpStatus DEFAULT_HTTP_STATUS = HttpStatus.UNAUTHORIZED;
    public static final String DEFAULT_HTTP_PATH = "AUTHORIZATION_EXCEPTION";

    public AuthenticationException (String message, String httpCode, HttpStatus httpStatus) {
        super(message, DEFAULT_HTTP_STATUS, String.join("-", DEFAULT_HTTP_PATH, httpCode));
    }

    public static class UserNotAuthenticated extends AuthenticationException {
        public UserNotAuthenticated(String message) {
            super(message, "AUTHENTICATION_ERROR", AuthenticationException.DEFAULT_HTTP_STATUS);
        }
    }
}
