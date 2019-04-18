package com.cosmin.webserver.files;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class DeleteFilesRoute extends IntegrationTest {

    @ParameterizedTest
    @ValueSource(strings = {"AEM.pdf", "test.txt", "kibana.png"})
    public void testDelete(String filename) throws Exception {
        Files.copy(Paths.get(SAMPLES_DIR + "/" + filename), Paths.get(SERVER_DIR + "/" + filename));

        HttpDelete delete = new HttpDelete("/files/" + filename);
        HttpResponse response = executeRequest(delete);

        assertEquals(200, response.getStatusLine().getStatusCode());
        assertFalse(Paths.get(SERVER_DIR + "/" + filename).toFile().isFile());
    }

    @ParameterizedTest
    @ValueSource(strings = {"AEM.pdf", "test.txt", "kibana.png"})
    public void testNotFoundWhenFileDoesNotExist(String filename) throws Exception {
        HttpDelete delete = new HttpDelete("/files/" + filename);
        HttpResponse response = executeRequest(delete);

        assertEquals(404, response.getStatusLine().getStatusCode());
    }
}
