package com.cosmin.webserver.files.functions;

import com.cosmin.webserver.files.FilesService;
import com.cosmin.webserver.http.controller.RequestFunction;
import com.cosmin.webserver.http.exception.BadRequestException;
import com.cosmin.webserver.http.protocol.Request;
import com.cosmin.webserver.http.protocol.Response;
import com.cosmin.webserver.http.protocol.Status;
import com.cosmin.webserver.http.protocol.body.BodyParams;
import com.cosmin.webserver.http.protocol.body.multipart.UploadedFile;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class CreateFile implements RequestFunction {
    private final static Logger logger = LoggerFactory.getLogger(CreateFile.class);

    private final FilesService filesService;

    @Override
    public Response apply(Request request) {
        BodyParams bodyParams = request.getParsedBody();
        UploadedFile file = bodyParams.get("file", UploadedFile.class)
                .orElseThrow(() -> new BadRequestException("Uploaded file is required."));

        logger.info("create file {}", file.getFileName());
        filesService.create(file);

        return new Response(Status.CREATED, "".getBytes());
    }
}
