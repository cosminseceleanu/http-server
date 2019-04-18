package com.cosmin.webserver.protocol;

import com.cosmin.webserver.http.handler.RequestParser;
import com.cosmin.webserver.http.protocol.*;
import com.cosmin.webserver.http.protocol.body.parser.DelegatingBodyParser;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class RequestParserTest {
    private String getRequest = "GET / HTTP/1.1\n" +
            "Host: localhost:9000\n" +
            "Connection: keep-alive\n" +
            "Pragma: no-cache\n" +
            "Cache-Control: no-cache\n" +
            "Upgrade-Insecure-Requests: 1\n" +
            "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36\n" +
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\n" +
            "Accept-Encoding: gzip, deflate, br\n" +
            "Accept-Language: en-US,en;q=0.9,ro;q=0.8\n" +
            "Cookie: Idea-7e88a7ef=df5216e3-5a8c-4eaf-af7a-e1acce45f44a\r\n\r\n";


    private String postRequest = "POST /test HTTP/1.1\n" +
            "Host: foo.example\n" +
            "Content-Type: application/x-www-form-urlencoded\n" +
            "Content-Length: 27\n" +
            "\n" +
            "field1=value1&field2=value2";

    private String multiPartRequest = "POST /files HTTP/1.1\n" +
            "    Host: localhost:8000\n" +
            "    User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:29.0) Gecko/20100101 Firefox/29.0\n" +
            "    Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n" +
            "Accept-Language: en-US,en;q=0.5\n" +
            "Accept-Encoding: gzip, deflate\n" +
            "Cookie: __atuvc=34%7C7; permanent=0; _gitlab_session=226ad8a0be43681acf38c2fab9497240; __profilin=p%3Dt; request_method=GET\n" +
            "Connection: keep-alive\n" +
            "Content-Type: multipart/form-data; boundary=---------------------------9051914041544843365972754266\n" +
            "Content-Length: 554\n" +
            "\n" +
            "-----------------------------9051914041544843365972754266\n" +
            "Content-Disposition: form-data; name=\"text\"\n" +
            "\n" +
            "text default\n" +
            "-----------------------------9051914041544843365972754266\n" +
            "Content-Disposition: form-data; name=\"file1\"; filename=\"a.txt\"\n" +
            "Content-Type: text/plain\n" +
            "\n" +
            "Content of a.txt.\n" +
            "\n" +
            "-----------------------------9051914041544843365972754266\n" +
            "Content-Disposition: form-data; name=\"file2\"; filename=\"a.html\"\n" +
            "Content-Type: text/html\n" +
            "\n" +
            "<!DOCTYPE html><title>Content of a.html.</title>\n" +
            "\n" +
            "-----------------------------9051914041544843365972754266--";

    @Test
    public void testParseGetRequest() throws Exception {
        RequestParser requestParser = createParser();
        Request request = requestParser.parse( new ByteArrayInputStream(getRequest.getBytes()));

        assertEquals(Method.GET, request.getMethod());
        assertEquals(HttpVersion.HTTP_V1_1, request.getHttpVersion());
        assertEquals("/", request.getUri());
    }

    @Test
    public void testParsePostRequest() throws Exception {
        RequestParser requestParser = createParser();
        Request request = requestParser.parse(new ByteArrayInputStream(postRequest.getBytes()));
        assertEquals(Method.POST, request.getMethod());
        assertEquals("/test", request.getUri());
        assertEquals(ContentType.APPLICATION_FORM_URLENCODED.toString(), request.getHeader(Header.CONTENT_TYPE));
        assertEquals("field1=value1&field2=value2", request.getBodyAsString());
    }


    @Test
    public void testParseMultiPartRequest() throws Exception {
        RequestParser requestParser = createParser();
        Request request = requestParser.parse(new ByteArrayInputStream(multiPartRequest.getBytes()));
        String body = request.getBodyAsString();


        assertEquals(Method.POST, request.getMethod());
        assertEquals("/files", request.getUri());
        assertTrue(request.getHeader(Header.CONTENT_TYPE).contains(ContentType.MULTIPART_FORM_DATA.toString()));
        assertTrue(body.startsWith("-----------------------------9051914041544843365972754266"));
    }

    private RequestParser createParser() {
        return new RequestParser(
                new RequestFactory(new DelegatingBodyParser(new ArrayList<>()))
        );
    }
}
