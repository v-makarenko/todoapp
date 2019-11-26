package com.example.todoapp.config;

import javax.ws.rs.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
    extends ResponseEntityExceptionHandler {

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<Object> handleAccessDeniedException(
        BadRequestException ex, WebRequest request) {
        return new ResponseEntity<>(
            "{\"msg\": " + ex.getMessage() + "}", new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}