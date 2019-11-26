package com.example.todoapp.config;

import com.example.todoapp.dto.ErrorDto;
import javax.ws.rs.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * All generic error handlers are defined here
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler
    extends ResponseEntityExceptionHandler {

    /**
     * This handles BadRequestException - which will happen in case of wrong input
     * @param ex exception happened
     * @param request web request
     * @return created ResponseEntity with error message and correct HTTP error code
     */
    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<Object> handleAccessDeniedException(
        BadRequestException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorDto(ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}