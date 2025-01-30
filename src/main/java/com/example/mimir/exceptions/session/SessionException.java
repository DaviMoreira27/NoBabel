package com.example.mimir.exceptions.session;

import com.example.mimir.exceptions.HttpClientException;
import org.springframework.http.HttpStatus;

public abstract class SessionException extends HttpClientException {
    public static final HttpStatus DEFAULT_HTTP_STATUS = HttpStatus.FORBIDDEN;
    public static final String DEFAULT_HTTP_PATH = "SESSION_EXCEPTION";

    public SessionException(String message, String httpCode) {
        super(message, DEFAULT_HTTP_STATUS, String.join("-", DEFAULT_HTTP_PATH, httpCode));
    }
}
