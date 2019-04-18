package com.cosmin.webserver.http.exception;

public class InvalidHttpRequestException extends RuntimeException {
    public InvalidHttpRequestException() {
    }

    public InvalidHttpRequestException(String message) {
        super(message);
    }

    public InvalidHttpRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
