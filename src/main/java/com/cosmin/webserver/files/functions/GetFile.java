package com.cosmin.webserver.files.functions;

import com.cosmin.webserver.files.FilesService;
import com.cosmin.webserver.http.controller.RequestFunction;
import com.cosmin.webserver.http.exception.InternalServerErrorException;
import com.cosmin.webserver.http.exception.NotFoundException;
import com.cosmin.webserver.http.protocol.Header;
import com.cosmin.webserver.http.protocol.Request;
import com.cosmin.webserver.http.protocol.Response;
import com.cosmin.webserver.http.protocol.Status;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RequiredArgsConstructor
public class GetFile implements RequestFunction {
    private final FilesService filesService;

    @Override
    public Response apply(Request request) {
        String filename = filesService.getFileNameFromUriPath(request.getUri());

        try {
            Path path = filesService.getPath(filename);
            if (!path.toFile().isFile()) {
                throw new NotFoundException("no file found for path " + filename);
            }
            String mimeType = path.toFile().toURI().toURL().openConnection().getContentType();

            return new Response(Status.OK, Files.readAllBytes(path))
                    .setHeader(Header.CONTENT_TYPE, mimeType);
        } catch (IOException e) {
            throw new InternalServerErrorException("error reading file " + filename);
        }
    }
}
