package com.cosmin.webserver.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public abstract class AbstractHandler implements SocketHandler {
    private Logger logger = LoggerFactory.getLogger(AbstractHandler.class);

    @Override
    public void handle(Socket clientSocket) {
        try (InputStream input = clientSocket.getInputStream(); OutputStream output = clientSocket.getOutputStream()) {
            doHandle(input, output);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            closeClientConnection(clientSocket);
        }
    }

    private void closeClientConnection(Socket clientSocket) {
        try {
            clientSocket.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    protected abstract void doHandle(InputStream input, OutputStream output) throws IOException;
}
