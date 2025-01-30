package com.example.mimir.dto;

import org.springframework.http.HttpStatus;

public record Error(HttpStatus httpStatus, String message, String httpPath) { }
