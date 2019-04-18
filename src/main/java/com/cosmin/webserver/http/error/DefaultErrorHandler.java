package com.cosmin.webserver.http.error;

import com.cosmin.webserver.http.exception.HttpException;
import com.cosmin.webserver.http.protocol.Header;
import com.cosmin.webserver.http.protocol.Response;
import com.cosmin.webserver.http.protocol.Status;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultErrorHandler implements ErrorHandler {
    private final String contentType;

    public DefaultErrorHandler() {
        this.contentType = "text/html";
    }

    @Override
    public Response handle(Exception e) {
        Status status = Status.INTERNAL_SERVER_ERROR;
        if (e instanceof HttpException) {
            status = ((HttpException) e).getStatus();
        }

        return new Response(status, e.getMessage().getBytes())
                .setHeader(Header.CONTENT_TYPE, "text/html")
                .setContentLength();
    }
}
