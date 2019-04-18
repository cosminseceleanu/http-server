package com.cosmin.webserver.http.controller.routing;

import com.cosmin.webserver.http.protocol.Request;
import com.cosmin.webserver.http.protocol.Response;

public interface Route {
    boolean supports(Request request);

    Response execute(Request request);
}
