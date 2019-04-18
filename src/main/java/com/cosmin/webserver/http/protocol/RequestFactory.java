package com.cosmin.webserver.http.protocol;

import com.cosmin.webserver.http.exception.InvalidHttpRequestException;
import com.cosmin.webserver.http.protocol.body.parser.BodyParser;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@RequiredArgsConstructor
public class RequestFactory {
    private final BodyParser bodyParser;

    public Request create(HttpVersion version, String uri, Method method, Map<String, String> headers, InputStream input) {
        return new Request(headers, method, uri, version, getBody(headers, input), bodyParser);
    }

    private byte[] getBody(Map<String, String> headers, InputStream input) {
        if (headers.containsKey(Header.TRANSFER_ENCODING.toString())) {
            throw new InvalidHttpRequestException("Http header " + Header.TRANSFER_ENCODING.toString() + " is not supported");
        }

        if (headers.containsKey(Header.CONTENT_LENGTH.toString())) {
            int contentLength = Integer.valueOf(headers.get(Header.CONTENT_LENGTH.toString()));

            return read(contentLength, input);
        }

        return new byte[0];
    }

    private byte[] read(int contentLength, InputStream inputStream) {
        try {
            byte[] body = new byte[contentLength];
            inputStream.read(body);

            return body;
        } catch (IOException e) {
            throw new InvalidHttpRequestException("Error while reading request body", e);
        }
    }
}
