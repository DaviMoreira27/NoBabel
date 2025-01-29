package com.example.mimir.exceptions.session;

import com.example.mimir.exceptions.HttpClientException;

abstract class SessionException extends HttpClientException {
    public static final int DEFAULT_HTTP_STATUS = 403;
    public static final String DEFAULT_HTTP_PATH = "SESSION_EXCEPTION";

    public SessionException(String message, String httpCode) {
        super(message, DEFAULT_HTTP_STATUS, String.join("-", DEFAULT_HTTP_PATH, httpCode));
    }
}
