package com.cosmin.webserver.files;

import com.cosmin.webserver.http.exception.BadRequestException;
import com.cosmin.webserver.http.exception.InternalServerErrorException;
import com.cosmin.webserver.http.exception.NotFoundException;
import com.cosmin.webserver.http.protocol.body.multipart.UploadedFile;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
public class FilesService {
    private final Logger logger = LoggerFactory.getLogger(FilesService.class);
    private final static String ROUTE_PREFIX = "/files";

    private final String rootDir;

    public byte[] get(String filename) throws IOException {
        Path path = getPath(getFileNameFromUriPath(filename));
        if (!path.toFile().isFile()) {
            throw new NotFoundException("no file found for path " + filename);
        }

        return Files.readAllBytes(path);
    }

    public void delete(String path) {
        Path filePath = getPath(getFileNameFromUriPath(path));
        if (!filePath.toFile().isFile()) {
            throw new NotFoundException("File not found");
        }

        try {
            Files.delete(filePath);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new InternalServerErrorException("Error deleting file.");
        }
    }

    public String getFileNameFromUriPath(String path) {
        return path.replace(ROUTE_PREFIX, "");
    }

    public void create(UploadedFile file) {
        String filename = file.getFileName();
        Path path = getPath(filename);
        if (path.toFile().isFile()) {
            throw new BadRequestException("File already exists on path " + path.toString());
        }

        try {
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            Files.write(path, file.getContent());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new InternalServerErrorException("Error writing file.");
        }
    }

    public void update(UploadedFile file, String uriPath) {
        String filename = getFileNameFromUriPath(uriPath);
        Path path = getPath(filename);
        if (!path.toFile().isFile()) {
            throw new NotFoundException("No file found for path " + path.toString());
        }

        try {
            Files.write(path, file.getContent());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new InternalServerErrorException("Error writing file.");
        }
    }

    public Path getPath(String filename) {
        return Paths.get(getFullPath(filename));
    }

    private String getFullPath(String filename) {
        return rootDir + "/" + filename;
    }
}
