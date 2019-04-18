package com.cosmin.webserver.http.protocol.body.multipart;

import lombok.Data;

@Data
public class UploadedFile {
    private final byte[] content;
    private final String fileName;
}
