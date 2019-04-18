package com.cosmin.webserver.http.protocol.body;


import lombok.Data;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Data
public class BodyParams {
    private final Map<String, Object> params;

    public BodyParams(Map<String, Object> params) {
        this.params = Collections.unmodifiableMap(params);
    }

    public Optional<Object> get(String name) {
        return Optional.ofNullable(params.get(name));
    }

    public <T> Optional<T> get(String name, Class<T> type) {
        return get(name).map(type::cast);
    }
}
