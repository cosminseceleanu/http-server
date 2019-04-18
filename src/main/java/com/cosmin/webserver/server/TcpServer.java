package com.cosmin.webserver.server;

import com.cosmin.webserver.server.handler.SocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(TcpServer.class);

    private final int serverPort;
    private final SocketHandler socketHandler;
    private int maxConnections = 5000;

    private ServerSocket serverSocket = null;
    private boolean isStopped = false;


    public TcpServer(int port, SocketHandler socketHandler) {
        this.serverPort = port;
        this.socketHandler = socketHandler;
    }

    public TcpServer(int serverPort, SocketHandler socketHandler, int maxConnections) {
        this.serverPort = serverPort;
        this.socketHandler = socketHandler;
        this.maxConnections = maxConnections;
    }

    public void run() {
        openServerSocket();

        while(!isStopped()) {
            try {
                Socket clientSocket = serverSocket.accept();
                socketHandler.handle(clientSocket);
            } catch (IOException e) {
                if (isStopped()) {
                    logger.info("TcpServer Stopped.");
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
        }

       logger.info("TcpServer Stopped.");
    }

    private void openServerSocket() {
        try {
            serverSocket = new ServerSocket(serverPort, maxConnections);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + serverPort, e);
        }
    }

    private synchronized boolean isStopped() {
        return isStopped;
    }

    public synchronized void stop() {
        isStopped = true;
        try {
            socketHandler.close();
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }
}