package com.cosmin.webserver.http.controller.routing.matchers;

import com.cosmin.webserver.http.controller.routing.RouteMatcher;
import com.cosmin.webserver.http.protocol.Method;
import com.cosmin.webserver.http.protocol.Request;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MethodMatcher implements RouteMatcher {
    private final Method method;

    @Override
    public boolean supports(Request request) {
        return request.getMethod() == method;
    }
}
