package com.cosmin.webserver.http.protocol.body.parser;

import com.cosmin.webserver.http.exception.ContentTypeNotSupportedException;
import com.cosmin.webserver.http.protocol.Header;
import com.cosmin.webserver.http.protocol.Request;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class DelegatingBodyParser implements BodyParser {
    private final List<BodyParser> parsers;

    @Override
    public boolean supports(Request request) {
        return true;
    }

    @Override
    public Map<String, Object> parse(Request request) {
        return parsers.stream()
                .filter(parser -> parser.supports(request))
                .findFirst()
                .orElseThrow(() -> new ContentTypeNotSupportedException(request.getHeader(Header.CONTENT_TYPE)))
                .parse(request);
    }
}
