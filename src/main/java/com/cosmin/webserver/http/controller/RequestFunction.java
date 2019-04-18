package com.cosmin.webserver.http.controller;

import com.cosmin.webserver.http.protocol.Request;
import com.cosmin.webserver.http.protocol.Response;

import java.util.function.Function;

@FunctionalInterface
public interface RequestFunction extends Function<Request, Response> {
}
