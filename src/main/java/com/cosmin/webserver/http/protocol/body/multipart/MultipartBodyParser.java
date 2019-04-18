package com.cosmin.webserver.http.protocol.body.multipart;

import com.cosmin.webserver.http.exception.InvalidHttpRequestException;
import com.cosmin.webserver.http.protocol.Header;
import com.cosmin.webserver.http.protocol.Request;
import com.cosmin.webserver.http.protocol.body.parser.BodyParser;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.cosmin.webserver.utils.InputStreamUtils.*;

public class MultipartBodyParser implements BodyParser {
    private final static Logger logger = LoggerFactory.getLogger(MultipartBodyParser.class);

    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String BOUNDARY_PREFIX = "--";
    private static final String BOUNDARY_SUFFIX = "--";

    @Override
    public boolean supports(Request request) {
        return request.isMultiPart();
    }

    @Override
    public Map<String, Object> parse(Request request) {
        try {
            NameValuePair contentType = NameValuePair.fromString(request.getHeader(Header.CONTENT_TYPE), "; ");
            String boundary = BOUNDARY_PREFIX + NameValuePair.fromString(contentType.value, "=").value;
            String endBoundary = boundary + BOUNDARY_SUFFIX;
            InputStream inputStream = new ByteArrayInputStream(request.getBody());

            return doParse(boundary, endBoundary, inputStream);
        } catch (IOException e) {
            throw new InvalidHttpRequestException(e.getMessage(), e);
        }
    }

    private Map<String, Object> doParse(String boundary, String endBoundary, InputStream inputStream) throws IOException {
        String nextLine;
        Map<String, Object> bodyParams = new HashMap<>();

        while (!(nextLine = getNextLineAsString(inputStream)).startsWith(endBoundary)) {
            if (nextLine.equals(boundary)) {
                continue;
            }
            if (nextLine.equals("")) {
                break;
            }
            if (!nextLine.startsWith(CONTENT_DISPOSITION)) {
                throw new InvalidHttpRequestException(
                        "Error parsing request multipart body! Line expected to contains content disposition"
                );
            }
            NameValuePair contextDisposition = NameValuePair.fromString(nextLine, ": ");
            String[] contentDispositionValues = contextDisposition.value.split("; ");
            NameValuePair fieldName = NameValuePair.fromString(contentDispositionValues[1], "=");

            if (contentDispositionValues.length == 2) {
                bodyParams.put(stripDoubleQuotes(fieldName.value), readSimpleValue(inputStream, boundary));
                continue;
            }
            NameValuePair fileName = NameValuePair.fromString(contentDispositionValues[2], "=");
            bodyParams.put(stripDoubleQuotes(fieldName.value), readFileValue(inputStream, boundary, fileName.value));
        }

        return bodyParams;
    }

    private String readSimpleValue(InputStream inputStream, String boundary) throws IOException {
        logger.info("start reading simple value from multipart request body");
        String value = new String(readValueUntilBoundary(inputStream, boundary));
        logger.info("end reading simple value from multipart request body");

        return stripDoubleQuotes(value);
    }

    private UploadedFile readFileValue(InputStream inputStream, String boundary, String filename) throws IOException {
        logger.info("start reading file {} from multipart request body", filename);
        byte [] value = readValueUntilBoundary(inputStream, boundary);
        logger.info("end reading file {} from multipart request body", filename);

        return new UploadedFile(value, stripDoubleQuotes(filename));
    }

    private byte[] readValueUntilBoundary(InputStream inputStream, String boundary) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        readLinesUntil(inputStream, ""); //@ToDo parse headers for multipart param

        do {
            ByteArrayOutputStream currentLine = readUntilLineFeed(inputStream);
            String lineAsString = currentLine.toString();
            if (lineAsString.startsWith(boundary) || lineAsString.equals("")) {
                break;
            }
            currentLine.writeTo(output);
        } while (true);

        byte[] content = output.toByteArray();

        return Arrays.copyOfRange(content, 0, content.length - 2); //removed last \r\n added from http
    }

    private String stripDoubleQuotes(String subject) {
        return subject.replace("\"", "");
    }

    @Data
    private static class NameValuePair {
        private final String name;
        private final String value;

        static NameValuePair fromString(String value, String regex) {
            String[] parts = value.split(regex);
            if (parts.length != 2) {
                throw new InvalidHttpRequestException(String.format(
                        "Unable to extract name value pair from %s using regex %s", value, regex
                ));
            }

            return new NameValuePair(parts[0], parts[1]);
        }
    }
}
