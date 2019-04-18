package com.cosmin.webserver.http.controller.routing;

import com.cosmin.webserver.http.protocol.Request;

@FunctionalInterface
public interface RouteMatcher {
    boolean supports(Request request);
}
