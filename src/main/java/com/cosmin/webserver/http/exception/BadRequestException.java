package com.cosmin.webserver.http.exception;

import com.cosmin.webserver.http.protocol.Status;

public class BadRequestException extends HttpException {
    public BadRequestException(String message) {
        super(message, Status.BAD_REQUEST);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause, Status.BAD_REQUEST);
    }
}
