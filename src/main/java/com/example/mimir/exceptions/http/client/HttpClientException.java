package com.example.mimir.exceptions.http.client;

import com.example.mimir.exceptions.AppException;

public class HttpClientException extends AppException {
    public HttpClientException (String message) {
        super(message);
    }
}
