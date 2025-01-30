package com.example.mimir.exceptions.general;

public class UnknownInternalException extends GeneralException {
    public UnknownInternalException(String message) {
        super(message, "UNKNOWN_INTERNAL_ERROR");
    }
}
