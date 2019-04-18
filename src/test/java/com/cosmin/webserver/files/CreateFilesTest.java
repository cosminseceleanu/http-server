package com.cosmin.webserver.files;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateFilesTest extends IntegrationTest {

    @ParameterizedTest
    @ValueSource(strings = {"AEM.pdf", "kibana.png", "test.txt"})
    public void testCreate(String filename) throws Exception {
        Path path = Paths.get(SAMPLES_DIR  + "/" + filename);
        HttpEntity multipart = MultipartEntityBuilder.create()
                .addBinaryBody("file", path.toFile(), ContentType.APPLICATION_OCTET_STREAM, filename)
                .build();
        HttpPost httpPost = new HttpPost("/files");
        httpPost.setEntity(multipart);

        HttpResponse httpResponse = executeRequest(httpPost);
        Path uploadedFile = Paths.get(SERVER_DIR + "/" + filename);

        assertEquals(201, httpResponse.getStatusLine().getStatusCode());
        assertTrue(uploadedFile.toFile().isFile());
    }
}
