package com.cosmin.webserver.server.handler;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

public interface SocketHandler extends Closeable {
    void handle(Socket clientSocket);

    @Override
    default void close() throws IOException {}
}
