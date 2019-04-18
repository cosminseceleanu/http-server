package com.cosmin.webserver.http.exception;


import com.cosmin.webserver.http.protocol.Status;

public class NotFoundException extends HttpException {
    public NotFoundException(String message) {
        super(message, Status.NOT_FOUND);
    }
}
