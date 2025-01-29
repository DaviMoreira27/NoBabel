package com.example.mimir.exceptions.auth;

import com.example.mimir.exceptions.HttpClientException;

abstract class AuthorizationException extends HttpClientException {
    public static final int DEFAULT_HTTP_STATUS = 401;
    public static final String DEFAULT_HTTP_PATH = "AUTHORIZATION_EXCEPTION";

    public AuthorizationException(String message, String httpCode) {
        super(message, DEFAULT_HTTP_STATUS, String.join("-", DEFAULT_HTTP_PATH, httpCode));
    }
}
