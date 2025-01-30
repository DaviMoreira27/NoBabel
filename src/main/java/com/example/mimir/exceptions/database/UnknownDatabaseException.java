package com.example.mimir.exceptions.database;

public class UnknownDatabaseException extends DatabaseException {
    public UnknownDatabaseException(String message) {
        super(message, "UNKNOWN_DATABASE_ERROR");
    }
}
