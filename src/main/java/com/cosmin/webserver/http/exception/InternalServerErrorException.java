package com.cosmin.webserver.http.exception;

import com.cosmin.webserver.http.protocol.Status;

public class InternalServerErrorException extends HttpException {
    public InternalServerErrorException(String message) {
        super(message, Status.INTERNAL_SERVER_ERROR);
    }

    public InternalServerErrorException(String message, Throwable cause) {
        super(message, cause, Status.INTERNAL_SERVER_ERROR);
    }
}
