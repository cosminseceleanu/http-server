package com.cosmin.webserver.http.controller.routing.matchers;

import com.cosmin.webserver.http.controller.routing.RouteMatcher;
import com.cosmin.webserver.http.protocol.Request;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class AndXMatcher implements RouteMatcher {
    private final Set<RouteMatcher> matchers;

    @Override
    public boolean supports(Request request) {
        for (RouteMatcher matcher : matchers) {
            if (!matcher.supports(request)) {
                return false;
            }
        }
        return true;
    }

    public static AndXMatcher of(RouteMatcher ...matchers) {
        return new AndXMatcher(new HashSet<>(Arrays.asList(matchers)));
    }
}
