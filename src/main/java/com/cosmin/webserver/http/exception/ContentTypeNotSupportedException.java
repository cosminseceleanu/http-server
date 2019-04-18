package com.cosmin.webserver.http.exception;

import com.cosmin.webserver.http.protocol.Status;

public class ContentTypeNotSupportedException extends HttpException {
    public ContentTypeNotSupportedException(String contentType) {
        super("Http content type " + contentType + " is not supported", Status.NOT_ACCEPTABLE);
    }
}
