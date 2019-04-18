package com.cosmin.webserver.files;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdateFilesTest extends IntegrationTest {

    @Test
    public void testUpdate() throws Exception {
        String filePath = SERVER_DIR + "/" + "old.txt";

        Files.write(Paths.get(filePath), "Hello World!".getBytes());
        Files.write(Paths.get(SERVER_DIR + "/" + "new.txt"), "Hello World New!".getBytes());

        HttpEntity multipart = MultipartEntityBuilder.create()
                .addBinaryBody("file", Paths.get(SERVER_DIR + "/" + "new.txt").toFile(), ContentType.APPLICATION_OCTET_STREAM, filePath)
                .build();
        HttpPut httpPut = new HttpPut("/files/old.txt");
        httpPut.setEntity(multipart);

        HttpResponse httpResponse = executeRequest(httpPut);

        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        assertEquals("Hello World New!", new String(Files.readAllBytes(Paths.get(filePath))));
    }

    @Test
    public void testNotFoundIsReceivedWhenFileToBeUpdatedNotExists() throws Exception {
        Path path = Paths.get(SAMPLES_DIR + "/test.txt");
        HttpEntity multipart = MultipartEntityBuilder.create()
                .addBinaryBody("file", path.toFile(), ContentType.APPLICATION_OCTET_STREAM, "test.txt")
                .build();
        HttpPut httpPost = new HttpPut("/files/old.txt");
        httpPost.setEntity(multipart);

        HttpResponse httpResponse = executeRequest(httpPost);
        assertEquals(404, httpResponse.getStatusLine().getStatusCode());
    }
}
