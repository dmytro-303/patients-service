package com.cgm.patients.controller;

import com.cgm.patients.domain.exception.PatientNotFoundException;
import com.cgm.patients.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({PatientNotFoundException.class, IllegalArgumentException.class})
    public ResponseEntity<?> handleInputException(Exception ex) {
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleServerException(RuntimeException ex) {
        return new ResponseEntity<>(
                new ErrorResponse("Internal server error happened"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}