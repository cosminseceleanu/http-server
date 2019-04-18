package com.cosmin.webserver.http.controller.routing.matchers;

import com.cosmin.webserver.http.controller.routing.RouteMatcher;
import com.cosmin.webserver.http.protocol.Request;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PathMatcher implements RouteMatcher {
    private final String supportedPath;

    @Override
    public boolean supports(Request request) {
        return request.getUri().equals(supportedPath);
    }
}
