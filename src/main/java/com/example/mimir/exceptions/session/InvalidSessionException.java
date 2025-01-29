package com.example.mimir.exceptions.session;

import com.example.mimir.exceptions.session.SessionException;

public class InvalidSessionException extends SessionException {
    public InvalidSessionException(String message) {
        super(message, "INVALID_SESSION");
    }
}
