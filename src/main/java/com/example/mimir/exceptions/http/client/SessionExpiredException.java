package com.example.mimir.exceptions.http.client;

public class SessionExpiredException extends AuthorizationException {
  public SessionExpiredException(String message) {
    super(message);
  }
}
