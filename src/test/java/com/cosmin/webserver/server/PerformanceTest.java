package com.cosmin.webserver.server;

import com.cosmin.webserver.Application;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PerformanceTest {
    private final static Logger logger = LoggerFactory.getLogger(PerformanceTest.class);

    @Test
    public void testSingleThread() {
        Application application = new Application();
        application.start();
        int nrOfRequests = 100;

        executeMultipleRequestsInParallel(nrOfRequests);

        application.stop();
    }

    @Test
    public void testMultiThreaded() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        Application application = new Application();
        application.start(5, "http-server", 10);

        int nrOfRequests = 100;
        executeMultipleRequestsInParallel(nrOfRequests);

        application.stop();
        executor.shutdown();
    }

    private void executeMultipleRequestsInParallel(int nrOfRequests) {

        List<Integer> responses = IntStream.range(0, nrOfRequests)
                .boxed()
                .map(i -> CompletableFuture.supplyAsync(() -> {
                    String response = executeRequest();
                    logger.info("request nr {} response {}", i, response);
                    return i;
                }))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        Assertions.assertEquals(nrOfRequests, responses.size());
    }


    private static String executeRequest() {
        HttpClient client = HttpClientBuilder.create().build();
        HttpHost httpHost = new HttpHost("localhost", 9000, "http");
        HttpGet get = new HttpGet("/hello");
        try {
            HttpResponse response = client.execute(httpHost, get);

            return new BasicResponseHandler().handleResponse(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
