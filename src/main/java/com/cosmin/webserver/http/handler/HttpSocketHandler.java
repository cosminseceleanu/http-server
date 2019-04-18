package com.cosmin.webserver.http.handler;


import com.cosmin.webserver.http.controller.FrontController;
import com.cosmin.webserver.http.exception.HttpException;
import com.cosmin.webserver.http.exception.InvalidHttpRequestException;
import com.cosmin.webserver.http.protocol.Request;
import com.cosmin.webserver.http.protocol.Response;
import com.cosmin.webserver.server.handler.AbstractHandler;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@AllArgsConstructor
public class HttpSocketHandler extends AbstractHandler {
    private final static Logger logger = LoggerFactory.getLogger(HttpSocketHandler.class);
    private final RequestParser requestParser;
    private final ResponseWriter responseWriter;
    private final FrontController frontController;

    @Override
    protected void doHandle(InputStream input, OutputStream output) throws IOException {
        try {
            Request request = requestParser.parse(input);
            logger.trace("request parsed {}", request.getUri());
            Response response = frontController.dispatch(request);
            logger.trace("response created {}", request.getUri());
            responseWriter.write(output, response);
            logger.trace("response sent {}", request.getUri());
        } catch (InvalidHttpRequestException e) {
            responseWriter.write(output, frontController.handleError(e));
        }
    }
}
