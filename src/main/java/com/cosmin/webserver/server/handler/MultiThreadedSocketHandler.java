package com.cosmin.webserver.server.handler;


import com.cosmin.webserver.utils.NamedThreadFactory;

import java.net.Socket;
import java.util.concurrent.*;

public class MultiThreadedSocketHandler implements SocketHandler {
    private final SocketHandler defaultHandler;
    private final ExecutorService executor;

    public MultiThreadedSocketHandler(SocketHandler defaultHandler, int coreSize, String threadName, int queueSize) {
       this(defaultHandler, coreSize, coreSize, threadName, queueSize);
    }

    public MultiThreadedSocketHandler(SocketHandler defaultHandler, int coreSize, int maxSize, String threadName, int queueSize) {
        this.defaultHandler = defaultHandler;
        BlockingQueue<Runnable> linkedBlockingDeque = new LinkedBlockingDeque<>(queueSize);
        executor = new ThreadPoolExecutor(
                coreSize,
                maxSize,
                30,
                TimeUnit.SECONDS,
                linkedBlockingDeque,
                new NamedThreadFactory(threadName)
        );
    }

    public MultiThreadedSocketHandler(SocketHandler defaultHandler, ExecutorService executor) {
        this.defaultHandler = defaultHandler;
        this.executor = executor;
    }

    @Override
    public void handle(Socket clientSocket) {
        executor.submit(() -> defaultHandler.handle(clientSocket));
    }

    @Override
    public void close() {
        executor.shutdown();
    }
}
