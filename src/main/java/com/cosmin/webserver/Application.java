package com.cosmin.webserver;

import com.cosmin.webserver.server.TcpServer;
import com.cosmin.webserver.server.handler.MultiThreadedSocketHandler;
import com.cosmin.webserver.server.handler.SocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Application {
    private final static Logger logger = LoggerFactory.getLogger(Application.class);

    private final ApplicationContainer container = new ApplicationContainer();
    private TcpServer server = null;

    public static void main(String ...args) {
        Application application = new Application();
        application.start(5, "file-server", 1000);
        logger.info("Application started with {} threads", 5);
    }

    public void start() {
        start(container.httpSocket);
    }

    public void start(int poolSize, String threadName, int queueSize) {
        start(poolSize, poolSize, threadName, queueSize);
    }

    public void start(int coreSize, int maxSize, String threadName, int queueSize) {
        SocketHandler multiThreadedSocketHandler = new MultiThreadedSocketHandler(
                container.httpSocket, coreSize, maxSize, threadName, queueSize
        );

        start(multiThreadedSocketHandler);
    }

    private synchronized void start(SocketHandler socketHandler) {
        server = new TcpServer(9000, socketHandler);
        new Thread(server).start();
    }

    public synchronized void stop() {
        server.stop();
    }
}
