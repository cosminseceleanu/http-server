package com.cosmin.webserver.http.controller.exception;

import com.cosmin.webserver.http.exception.HttpException;
import com.cosmin.webserver.http.protocol.Method;
import com.cosmin.webserver.http.protocol.Status;

public class RouteNotFoundException extends HttpException {
    public RouteNotFoundException(String path, Method method) {
        super(String.format("No route found for path %s and http method %s", path, method.toString()), Status.NOT_FOUND);
    }
}
