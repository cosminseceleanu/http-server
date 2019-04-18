package com.cosmin.webserver.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamUtils {
    private final static Logger logger = LoggerFactory.getLogger(InputStreamUtils.class);

    public static final int CARRIAGE_RETURN = '\r';
    public static final int LINE_FEED = '\n';

    public static String readLinesUntil(InputStream inputStream, String value) throws IOException {
        StringBuilder buffer = new StringBuilder();
        String line;
        while (!(line = getNextLineAsString(inputStream)).equals(value)) {
            buffer.append(line);
        }

        return buffer.toString();
    }

    public static String getNextLineAsString(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        int nextByte;
        while ((nextByte = inputStream.read()) != -1 && !isReturn(nextByte, inputStream)) {
            builder.append((char) nextByte);
        }
        String line = builder.toString();
        logger.trace("extracted line from input stream {}", line);

        return line;
    }

    public static ByteArrayOutputStream getNextLine(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int nextByte;
        while ((nextByte = inputStream.read()) != -1 && !isReturn(nextByte, inputStream)) {
            outputStream.write(nextByte);
        }

        return outputStream;
    }

    private static boolean isReturn(int currentByte, InputStream inputStream) throws IOException {
        return currentByte == LINE_FEED || (currentByte == CARRIAGE_RETURN && skipNextCharacterIfLineFeed(inputStream));
    }

    private static boolean skipNextCharacterIfLineFeed(InputStream inputStream) throws IOException {
        inputStream.mark(1);
        if (inputStream.read() != LINE_FEED) {
            inputStream.reset();
        }
        return true;
    }

    public static ByteArrayOutputStream readUntilLineFeed(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int nextByte;
        while ((nextByte = inputStream.read()) != -1) {
            outputStream.write(nextByte);
            if (nextByte == LINE_FEED) {
                break;
            }
        }

        return outputStream;
    }
}
