package com.cosmin.webserver.http.protocol.body.parser;

import com.cosmin.webserver.http.protocol.Request;

import java.util.Map;

public interface BodyParser {
    boolean supports(Request request);

    Map<String, Object> parse(Request request);
}
