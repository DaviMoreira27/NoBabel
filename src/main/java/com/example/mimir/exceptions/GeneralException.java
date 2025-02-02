package com.example.mimir.exceptions;

import org.springframework.http.HttpStatus;

public abstract class GeneralException extends HttpClientException {
    public static HttpStatus DEFAULT_HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;
    public static final String DEFAULT_HTTP_PATH = "GENERAL_ERROR";

    public GeneralException (String message, String httpCode, HttpStatus httpStatus) {
        super(message, DEFAULT_HTTP_STATUS, String.join("-", DEFAULT_HTTP_PATH, httpCode));
    }

    public static class UnknownInternalException extends GeneralException {
        public UnknownInternalException(String message) {
            super(message, "UNKNOWN_INTERNAL_ERROR", GeneralException.DEFAULT_HTTP_STATUS);
        }
    }

}
