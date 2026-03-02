package com.camilagksantos.finance.adapters.inbound.rest.handler;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    private ErrorResponse() {}

    public static ErrorResponse of(int status, String error, String message, String path) {
        ErrorResponse response = new ErrorResponse();
        response.timestamp = LocalDateTime.now();
        response.status = status;
        response.error = error;
        response.message = message;
        response.path = path;
        return response;
    }
}
