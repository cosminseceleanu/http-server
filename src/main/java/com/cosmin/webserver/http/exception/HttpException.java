package com.cosmin.webserver.http.exception;

import com.cosmin.webserver.http.protocol.Status;

public class HttpException extends RuntimeException {
    private Status status;

    public HttpException(String message, Status status) {
        super(message);
        this.status = status;
    }

    public HttpException(String message, Throwable cause, Status status) {
        super(message, cause);
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
