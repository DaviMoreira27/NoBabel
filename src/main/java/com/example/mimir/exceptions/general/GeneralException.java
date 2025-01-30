package com.example.mimir.exceptions.general;

import com.example.mimir.exceptions.HttpClientException;
import org.springframework.http.HttpStatus;

public abstract class GeneralException extends HttpClientException {
    public static final HttpStatus DEFAULT_HTTP_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;
    public static final String DEFAULT_HTTP_PATH = "GENERAL_ERROR";

    public GeneralException (String message, String httpCode) {
        super(message, DEFAULT_HTTP_STATUS, String.join("-", DEFAULT_HTTP_PATH, httpCode));
    }
}
