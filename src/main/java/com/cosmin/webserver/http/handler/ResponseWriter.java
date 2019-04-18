package com.cosmin.webserver.http.handler;

import com.cosmin.webserver.http.protocol.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;

public class ResponseWriter {
    private static final String NEW_LINE = "\r\n";
    private final static Logger logger = LoggerFactory.getLogger(ResponseWriter.class);

    public void write(OutputStream outputStream, Response response) throws IOException {
        writeLn(response.getVersion().toString() + " " + response.getStatus().toString(), outputStream);
        response.getHeaders().forEach((name, value) ->  {
            writeLn(response.headerLine(name, value), outputStream);
        });
        writeLn("", outputStream);

        outputStream.write(response.getBody());
    }

    private void writeLn(String value, OutputStream outputStream) {
        try {
            logger.trace("write response line " + value);
            outputStream.write((value + NEW_LINE).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e); //@ToDo remove this
        }
    }
}
