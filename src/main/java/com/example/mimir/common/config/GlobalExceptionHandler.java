package com.example.mimir.common.config;

import com.example.mimir.dto.Error;
import com.example.mimir.exceptions.HttpClientException;
import com.example.mimir.exceptions.database.DatabaseConnectionException;
import com.example.mimir.exceptions.database.DatabaseException;
import com.example.mimir.exceptions.session.SessionException;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.logging.Logger;

@Order( Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ DatabaseException.class})
    public ResponseEntity<Object> handleDatabaseExceptions(DatabaseConnectionException exception) {
        return ResponseEntity
                .status(exception.getStatus())
                .body(new Error(exception.getStatus(), exception.getMessage(), exception.getHttpPath()));
    }

    @ExceptionHandler({ SessionException.class })
    public ResponseEntity<Object> handleSessionExceptions(SessionException exception) {
        return ResponseEntity
                .status(exception.getStatus())
                .body(new Error(exception.getStatus(), exception.getMessage(), exception.getHttpPath()));
    }

    @ExceptionHandler({ HttpClientException.class })
    public ResponseEntity<Object> handleHttpClientExceptions(HttpClientException exception) {
        return ResponseEntity
                .status(exception.getStatus())
                .body(new Error(exception.getStatus(), exception.getMessage(), exception.getHttpPath()));
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handleRuntimeException(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new Error(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(),
                        "GENERAL_INTERNAL_SERVER_ERROR"));
    }
}