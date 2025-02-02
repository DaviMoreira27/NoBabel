package com.example.mimir.exceptions;

import org.springframework.http.HttpStatus;

public abstract class DatabaseException extends HttpClientException {
    public static HttpStatus DEFAULT_HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;
    public static final String DEFAULT_HTTP_PATH = "DATABASE_ERROR";

    public DatabaseException(String message, String httpCode, HttpStatus httpStatus) {
        super(message, DEFAULT_HTTP_STATUS, String.join("-", DEFAULT_HTTP_PATH, httpCode));
    }

    public static class UnknownDatabaseException extends DatabaseException {
        public UnknownDatabaseException(String message) {
            super(message, "UNKNOWN_DATABASE_ERROR", DatabaseException.DEFAULT_HTTP_STATUS);
        }
    }

    public static class DatabaseConnectionException extends DatabaseException {
        public DatabaseConnectionException(String message) {
            super(message, "CONNECTION_ERROR", DatabaseException.DEFAULT_HTTP_STATUS);
        }
    }
}
