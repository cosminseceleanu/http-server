package com.cosmin.webserver.http.protocol;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
public enum Header {
    USER_AGENT("User-Agent"),
    CONNECTION("Connection"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type"),
    TRANSFER_ENCODING("Transfer-Encoding"),
    HOST("Host");

    private final String header;

    @Override
    public String toString() {
        return header;
    }
}
