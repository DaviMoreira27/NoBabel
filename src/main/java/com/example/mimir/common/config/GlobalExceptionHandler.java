package com.example.mimir.common.config;

import com.example.mimir.dto.Error;
import com.example.mimir.exceptions.GeneralException;
import com.example.mimir.exceptions.HttpClientException;
import com.example.mimir.exceptions.DatabaseException;
import com.example.mimir.exceptions.SessionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order( Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    private String jsonConverter (Object bodyResponse) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        return writer.writeValueAsString(bodyResponse);
    }


    @ExceptionHandler({ DatabaseException.class})
    public ResponseEntity<String> handleDatabaseExceptions(DatabaseException exception) throws JsonProcessingException {
        Error errorBody = new Error(exception.getStatus(), exception.getMessage(),
                exception.getHttpPath());
        System.out.println("ERROR BODY DATABASE " + errorBody);
        return ResponseEntity
                .status(exception.getStatus())
                .body(this.jsonConverter(errorBody));
    }

    @ExceptionHandler({ SessionException.class })
    public ResponseEntity<String> handleSessionExceptions(SessionException exception) throws JsonProcessingException {
        Error errorBody = new Error(exception.getStatus(), exception.getMessage(),
                exception.getHttpPath());
        System.out.println("ERROR BODY " + errorBody);
        return ResponseEntity
                .status(exception.getStatus())
                .body(this.jsonConverter(errorBody));
    }

    @ExceptionHandler({ HttpClientException.class })
    public ResponseEntity<String> handleHttpClientExceptions(HttpClientException exception) throws JsonProcessingException {
        Error errorBody = new Error(exception.getStatus(), exception.getMessage(),
                exception.getHttpPath());
        System.out.println("ERROR BODY GENERIC" + errorBody);
        return ResponseEntity
                .status(exception.getStatus())
                .body(this.jsonConverter(errorBody));
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<String> handleRuntimeException(RuntimeException exception) throws JsonProcessingException {
        GeneralException.UnknownInternalException errorBody = new GeneralException.UnknownInternalException("Sorry, an internal error " +
                "has ocurred, please try again later!");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(this.jsonConverter(errorBody));
    }
}