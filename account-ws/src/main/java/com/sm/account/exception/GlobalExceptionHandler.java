package com.sm.account.exception;

import com.sm.account.controller.response.ErrorResponse;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @Bean
    public ErrorAttributes errorAttributes() {
        // Hide exception field in the return object
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
                return super.getErrorAttributes(webRequest, ErrorAttributeOptions.defaults()
                        .including(ErrorAttributeOptions.Include.MESSAGE)
                        .excluding(ErrorAttributeOptions.Include.EXCEPTION));
            }
        };
    }

    @ExceptionHandler({ValidationException.class})
    public void handleCustomException(HttpServletResponse res, ValidationException ex) throws IOException {
        res.sendError(ex.getHttpStatus().value(), ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(HttpServletResponse res) throws IOException {
        res.sendError(HttpStatus.FORBIDDEN.value(), "Access denied");
    }

    /**
     * Handle unprocessed json data exception
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), "Malformed JSON request", ((ServletWebRequest) request).getRequest().getRequestURI());
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), ex.getMessage(), ((ServletWebRequest) request).getRequest().getRequestURI());
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("MethodArgumentNotValidException------> " + exception.getMessage(), exception);
        ErrorResponse errorDto = new ErrorResponse();
        if (!exception.getBindingResult().getFieldErrors().isEmpty()) {
            errorDto = exception.getBindingResult().getFieldErrors().stream().findFirst()
                    .map(err -> {
                        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), err.getDefaultMessage(), ((ServletWebRequest) request).getRequest().getRequestURI());
                        return error;
                    }).get();
        }
        return new ResponseEntity<>(errorDto, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

//  @ExceptionHandler(ConstraintViolationException.class)
//  public void handleConstraintViolationException(ConstraintViolationException exception, HttpServletResponse res) throws IOException {
//    logger.error("ConstraintViolationException------> " + exception.getMessage(), exception);
//    int index = exception.getMessage().indexOf(":");
//    String message = exception.getMessage();
//    if (index != -1) {
//      message = message.substring(index + 2);
//    }
//
//    res.sendError(HttpStatus.BAD_REQUEST.value(), message);
//  }

    @ExceptionHandler(Exception.class)
    public void handleException(Exception exception, HttpServletResponse res) throws IOException {
        logger.error("Exception------> " + exception.getMessage(), exception);
        res.sendError(HttpStatus.BAD_REQUEST.value(), "Something went wrong");
    }


}
