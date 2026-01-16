package com.example.demo.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public record NonExistingUseResponse(String status, String message){}

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<NonExistingUseResponse> handleHttpClientErrorException(HttpClientErrorException e) {
        var statusCode = e.getStatusCode();
        var errorResponse = new NonExistingUseResponse(statusCode.toString(), e.getMessage());
        return new ResponseEntity<>(errorResponse, statusCode);
    }
}
