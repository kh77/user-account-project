package com.sm.accountstatement.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final String message;
  private final HttpStatus httpStatus;
  private Exception exception;

  public ValidationException(String message, HttpStatus httpStatus) {
    this.message = message;
    this.httpStatus = httpStatus;
  }

  public ValidationException(String message) {
    this.message = message;
    this.httpStatus = HttpStatus.BAD_REQUEST;
  }

  public ValidationException(String message, HttpStatus httpStatus, Exception exception) {
    this.message = message;
    this.httpStatus = httpStatus;
    this.exception = exception;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public Exception getException() {
    return exception;
  }
}
