package com.cosmin.webserver.http.protocol;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
public enum Method {
    GET("GET"), 
    HEAD("HEAD"), 
    POST("POST"), 
    PUT("PUT"), 
    PATCH("PATCH"),
    DELETE("DELETE"),
    TRACE("TRACE"), 
    CONNECT("CONNECT");

    private final String method;

    @Override
    public String toString() {
        return method;
    }
}
