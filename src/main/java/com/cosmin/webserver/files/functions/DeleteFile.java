package com.cosmin.webserver.files.functions;

import com.cosmin.webserver.files.FilesService;
import com.cosmin.webserver.http.controller.RequestFunction;
import com.cosmin.webserver.http.protocol.Request;
import com.cosmin.webserver.http.protocol.Response;
import com.cosmin.webserver.http.protocol.Status;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteFile implements RequestFunction {
    private final FilesService filesService;

    @Override
    public Response apply(Request request) {
        filesService.delete(request.getUri());

        return new Response(Status.OK, "".getBytes());
    }
}
