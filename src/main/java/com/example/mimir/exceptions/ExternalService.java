package com.example.mimir.exceptions;

public abstract class ExternalService {
    public String externalService;
    public int externalServiceErrorCode;
    public String externalServiceMessage;

    ExternalService(String externalService, int externalServiceErrorCode, String externalServiceMessage) {
        this.externalService = externalService;
        this.externalServiceErrorCode = externalServiceErrorCode;
        this.externalServiceMessage = externalServiceMessage;
    }
}
