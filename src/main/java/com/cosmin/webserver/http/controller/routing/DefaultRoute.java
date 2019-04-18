package com.cosmin.webserver.http.controller.routing;

import com.cosmin.webserver.http.protocol.Request;
import com.cosmin.webserver.http.protocol.Response;
import lombok.AllArgsConstructor;

import java.util.function.Function;

@AllArgsConstructor
public class DefaultRoute implements Route {
    private final RouteMatcher routeMatcher;
    private final Function<Request, Response> function;

    @Override
    public boolean supports(Request request) {
        return routeMatcher.supports(request);
    }

    @Override
    public Response execute(Request request) {
        return function.apply(request);
    }
}
