package com.cosmin.webserver.http.controller;

import com.cosmin.webserver.http.controller.exception.RouteNotFoundException;
import com.cosmin.webserver.http.controller.routing.Route;
import com.cosmin.webserver.http.error.ErrorHandler;
import com.cosmin.webserver.http.protocol.Request;
import com.cosmin.webserver.http.protocol.Response;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

@AllArgsConstructor
public class FrontController {
    private final static Logger logger = LoggerFactory.getLogger(FrontController.class);

    private final ErrorHandler errorHandler;
    private final Set<Route> routes;

    public Response dispatch(Request request) {
        try {
            Route route = routes.stream()
                    .filter(r -> r.supports(request))
                    .findFirst()
                    .orElseThrow(() -> new RouteNotFoundException(request.getUri(), request.getMethod()));
            logger.debug("found route {} for request {}", route.getClass().getName(), request.getUri());
            return route.execute(request)
                    .setContentLength();
        } catch (Exception e) {
            return handleError(e);
        }
    }

    public Response handleError(Exception e) {
        logger.info("handle error", e);
        return errorHandler.handle(e);
    }
}
