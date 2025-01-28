package com.example.mimir.exceptions.http.client;

abstract class AuthorizationException extends RuntimeException {
    public AuthorizationException(String message) {
        super(message);
    }
}

