package com.example.mimir.exceptions.session;

import com.example.mimir.exceptions.session.SessionException;

public class SessionExpiredException extends SessionException {
  public SessionExpiredException(String message) {
    super(message, "SESSION_EXPIRED");
  }
}
