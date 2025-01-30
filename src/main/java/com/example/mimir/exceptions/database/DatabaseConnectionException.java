package com.example.mimir.exceptions.database;

public class DatabaseConnectionException extends DatabaseException {
    public DatabaseConnectionException(String message) {
        super(message, "CONNECTION_ERROR");
    }
}
