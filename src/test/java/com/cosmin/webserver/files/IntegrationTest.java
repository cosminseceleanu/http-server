package com.cosmin.webserver.files;

import com.cosmin.webserver.Application;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;


public class IntegrationTest {
    private static Application application;
    private static HttpHost defaultHost;

    private static final String ROOT_DIR = "src/test/resources";
    protected static final String SERVER_DIR = ROOT_DIR + "/server";
    protected static final String SAMPLES_DIR = ROOT_DIR + "/samples";

    @BeforeAll
    static void setup() throws Exception {
        Path serverRootDir = Paths.get(SERVER_DIR);
        if (!Files.exists(serverRootDir)) {
            Files.createDirectories(serverRootDir);
        }
        deleteFilesFromDirectory(Paths.get(SERVER_DIR));
        System.setProperty("file.root_dir", SERVER_DIR);
        application = new Application();
        application.start(5, "http-server", 10);
        defaultHost = new HttpHost("localhost", 9000, "http");
    }

    private static void deleteFilesFromDirectory(Path directory) {
        try {
            Files.walk(directory)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .filter(File::isFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void afterClass() {
        application.stop();
    }

    protected HttpResponse executeRequest(HttpRequest request) throws Exception {
        return executeRequest(request, defaultHost);
    }

    protected HttpResponse executeRequest(HttpRequest request, HttpHost httpHost) throws Exception {
        HttpClient client = HttpClientBuilder.create().build();

        return client.execute(httpHost, request);
    }
}
