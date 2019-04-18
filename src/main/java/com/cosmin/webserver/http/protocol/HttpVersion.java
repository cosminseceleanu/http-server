package com.cosmin.webserver.http.protocol;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HttpVersion {
    HTTP_V1("HTTP/1.0"),
    HTTP_V1_1("HTTP/1.1");

    private final String value;

    public static HttpVersion fromString(String text) {
        for (HttpVersion httpVersion : HttpVersion.values()) {
            if (httpVersion.getValue().equalsIgnoreCase(text)) {
                return httpVersion;
            }
        }
        throw new RuntimeException("Invalid value for enum: " + HttpVersion.class.getName() + text);
    }

    @Override
    public String toString() {
        return value;
    }
}
