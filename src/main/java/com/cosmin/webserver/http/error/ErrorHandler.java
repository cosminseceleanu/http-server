package com.cosmin.webserver.http.error;

import com.cosmin.webserver.http.protocol.Response;

public interface ErrorHandler {
    Response handle(Exception e);
}
