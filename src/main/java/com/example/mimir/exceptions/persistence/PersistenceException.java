package com.example.mimir.exceptions.persistence;

import com.example.mimir.exceptions.AppException;

public class PersistenceException extends AppException {
    public PersistenceException (String message) {
        super(message);
    }
}
