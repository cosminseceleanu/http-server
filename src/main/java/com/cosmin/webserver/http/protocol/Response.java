package com.cosmin.webserver.http.protocol;

import lombok.Data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Data
public class Response {
    private final Status status;
    private final HttpVersion version;
    private final Map<String, String> headers;
    private final byte[] body;

    public Response(Status status, HttpVersion version, Map<String, String> headers, byte[] body) {
        this.status = status;
        this.version = version;
        this.headers = Collections.unmodifiableMap(headers);
        this.body = body;
    }

    public Response(Status status, Map<String, String> headers, byte[] body) {
        this.status = status;
        this.version = HttpVersion.HTTP_V1_1;
        this.body = body;
        this.headers = Collections.unmodifiableMap(headers);
    }

    public Response(Status status, byte[] body) {
        this.status = status;
        this.version = HttpVersion.HTTP_V1_1;
        this.body = body;
        this.headers = Collections.unmodifiableMap(new HashMap<>());
    }

    public Response setContentLength() {
        return setHeader(Header.CONTENT_LENGTH, String.valueOf(body.length));
    }

    public Response setHeader(Header header, String value) {
        return setHeader(header.toString(), value);
    }

    public Response setHeader(String header, String value) {
        Map<String, String> currentHeaders = new HashMap<>(headers);
        currentHeaders.put(header, value);

        return new Response(status, version, currentHeaders, body);
    }



    public String headerLine(Header header, String value) {
        return headerLine(header.toString(), value);
    }

    public String headerLine(String name, String value) {
        return String.format("%s: %s", name, value);
    }
}
