package com.example.mimir.exceptions.external.service;

import com.example.mimir.exceptions.AppException;

public class ExternalServiceException extends AppException {
    public ExternalServiceException (String message) {
        super(message);
    }
}
