package com.cosmin.webserver.http.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ContentType {
    MULTIPART_FORM_DATA("multipart/form-data"),
    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded"),
    TEXT_PLAIN("text/plain");

    private final String type;

    @Override
    public String toString() {
        return type;
    }
}
