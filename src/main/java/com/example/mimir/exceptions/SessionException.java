package com.example.mimir.exceptions;

import org.springframework.http.HttpStatus;

public abstract class SessionException extends HttpClientException {
    public static HttpStatus DEFAULT_HTTP_STATUS = HttpStatus.FORBIDDEN;
    public static final String DEFAULT_HTTP_PATH = "SESSION_EXCEPTION";

    public SessionException(String message, String httpCode, HttpStatus httpStatus) {
        super(message, DEFAULT_HTTP_STATUS, String.join("-", DEFAULT_HTTP_PATH, httpCode));
    }

    public static class InvalidSessionException extends SessionException {
        public static HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        public InvalidSessionException(String message) {
            super(message, "INVALID_SESSION", httpStatus);
        }
    }

    public static class SessionExpiredException extends SessionException {
        public static HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        public SessionExpiredException(String message) {
            super(message, "SESSION_EXPIRED", httpStatus);
        }
    }

    public static class NoCookiesFound extends SessionException {
        public static HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        public NoCookiesFound(String message) {
            super(message, "SESSION_EXPIRED", httpStatus);
        }
    }

}
