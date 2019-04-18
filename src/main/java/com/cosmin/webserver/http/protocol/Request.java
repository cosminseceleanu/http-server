package com.cosmin.webserver.http.protocol;

import com.cosmin.webserver.http.exception.InternalServerErrorException;
import com.cosmin.webserver.http.protocol.body.BodyParams;
import com.cosmin.webserver.http.protocol.body.parser.BodyParser;
import lombok.Data;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Data
public class Request {
    private final Map<String, String> headers;
    private final Method method;
    private final String uri;
    private final HttpVersion httpVersion;
    private final byte[] body;
    private final BodyParser bodyParser;

    public Request(Map<String, String> headers, Method method, String uri, HttpVersion httpVersion, byte[] body, BodyParser bodyParser) {
        this.headers = Collections.unmodifiableMap(headers);
        this.method = method;
        this.uri = uri;
        this.httpVersion = httpVersion;
        this.body = body;
        this.bodyParser = bodyParser;
    }

    public boolean isMultiPart() {
        return Optional.ofNullable(getHeader(Header.CONTENT_TYPE))
                .map(value -> value.startsWith(ContentType.MULTIPART_FORM_DATA.toString()))
                .orElse(false);
    }

    public boolean hasHeader(Header header) {
        return headers.containsKey(header.toString());
    }

    public String getHeader(Header header) {
        return headers.get(header.toString());
    }

    public String getBodyAsString() {
        return new String(getBody());
    }

    public BodyParams getParsedBody() {
        if (!bodyParser.supports(this)) {
            throw new InternalServerErrorException("No body parser was found for current request.");
        }

        return new BodyParams(bodyParser.parse(this));
    }
}
