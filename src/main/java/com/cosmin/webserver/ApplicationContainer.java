package com.cosmin.webserver;

import com.cosmin.webserver.files.FilesService;
import com.cosmin.webserver.files.FileRoutes;
import com.cosmin.webserver.http.controller.FrontController;
import com.cosmin.webserver.http.controller.routing.DefaultRoute;
import com.cosmin.webserver.http.controller.routing.Route;
import com.cosmin.webserver.http.controller.routing.matchers.PathMatcher;
import com.cosmin.webserver.http.error.DefaultErrorHandler;
import com.cosmin.webserver.http.error.ErrorHandler;
import com.cosmin.webserver.http.handler.HttpSocketHandler;
import com.cosmin.webserver.http.handler.RequestParser;
import com.cosmin.webserver.http.handler.ResponseWriter;
import com.cosmin.webserver.http.protocol.*;
import com.cosmin.webserver.http.protocol.body.multipart.MultipartBodyParser;
import com.cosmin.webserver.http.protocol.body.parser.BodyParser;
import com.cosmin.webserver.http.protocol.body.parser.DelegatingBodyParser;
import com.cosmin.webserver.server.handler.SocketHandler;
import com.cosmin.webserver.utils.ThreadUtils;

import java.util.*;
import java.util.function.Function;

public class ApplicationContainer {

    public final BodyParser bodyParser = new DelegatingBodyParser(Arrays.asList(
            new MultipartBodyParser()
    ));
    public final RequestFactory requestFactory = new RequestFactory(bodyParser);
    public final RequestParser parser = new RequestParser(requestFactory);
    public final ResponseWriter responseWriter = new ResponseWriter();
    public final ErrorHandler errorHandler = new DefaultErrorHandler();


    public final FilesService filesService = new FilesService(System.getProperty("file.root_dir", "files"));
    public final Set<Route> routes = new HashSet<>(Arrays.asList(
        FileRoutes.getRoute(filesService),
        FileRoutes.createRoute(filesService),
        FileRoutes.deleteRoute(filesService),
        FileRoutes.updateRoute(filesService),
        new DefaultRoute(new PathMatcher("/hello"), helloWorld())
    ));

    public final FrontController frontController = new FrontController(errorHandler, routes);
    public final SocketHandler httpSocket = new HttpSocketHandler(parser, responseWriter, frontController);

    private Function<Request, Response> helloWorld() {
        return request -> {
            Map<String, String> headers = new HashMap<>();
            headers.put(Header.CONTENT_TYPE.toString(), "text/html; charset=UTF-8");

            String body = "<p>Hello World</p>";
            ThreadUtils.randomSleep(200);

            return new Response(Status.OK, headers, body.getBytes());
        };
    }
}
