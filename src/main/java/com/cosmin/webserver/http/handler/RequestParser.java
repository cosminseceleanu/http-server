package com.cosmin.webserver.http.handler;

import com.cosmin.webserver.http.exception.InvalidHttpRequestException;
import com.cosmin.webserver.http.protocol.HttpVersion;
import com.cosmin.webserver.http.protocol.Method;
import com.cosmin.webserver.http.protocol.Request;
import com.cosmin.webserver.http.protocol.RequestFactory;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.cosmin.webserver.utils.InputStreamUtils.getNextLineAsString;

@AllArgsConstructor
public class RequestParser {
    private final RequestFactory requestFactory;

    public Request parse(InputStream inputStream) {
        try {
            RequestLine requestLine = parseRequestLine(inputStream);
            Map<String, String> headers = parseHeaders(inputStream);

            return requestFactory.create(requestLine.httpVersion, requestLine.uri, requestLine.method, headers, inputStream);
        } catch (Exception e) {
            throw new InvalidHttpRequestException(e.getMessage(), e);
        }
    }

    private RequestLine parseRequestLine(InputStream inputStream) throws IOException {
        String line = getNextLineAsString(inputStream);
        String[] split = line.split("\\s+");
        if (split.length != 3) {
            throw new InvalidHttpRequestException("Http request first line is invalid " + line);
        }

        return new RequestLine(Method.valueOf(split[0]), split[1], HttpVersion.fromString(split[2]));
    }

    private Map<String, String> parseHeaders(InputStream inputStream) throws IOException {
        Map<String, String> requestHeaders = new HashMap<>();
        String nextLine;
        while (!(nextLine = getNextLineAsString(inputStream)).equals("")) {
            String[] header = this.parseHeader(nextLine);
            requestHeaders.put(header[0], header[1]);
        }
        return requestHeaders;
    }

    private String[] parseHeader(String headerLine) {
        String[] headerParts = headerLine.split(":\\s+");
        if (headerParts.length != 2) {
            throw new InvalidHttpRequestException("Http header line is invalid " + headerLine);
        }
        return new String[] { headerParts[0], headerParts[1] };
    }

    @AllArgsConstructor
    private class RequestLine {
        private final Method method;
        private final String uri;
        private final HttpVersion httpVersion;
    }
}
