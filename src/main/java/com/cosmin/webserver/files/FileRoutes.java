package com.cosmin.webserver.files;

import com.cosmin.webserver.files.functions.CreateFile;
import com.cosmin.webserver.files.functions.DeleteFile;
import com.cosmin.webserver.files.functions.GetFile;
import com.cosmin.webserver.files.functions.UpdateFile;
import com.cosmin.webserver.http.controller.routing.DefaultRoute;
import com.cosmin.webserver.http.controller.routing.Route;
import com.cosmin.webserver.http.controller.routing.matchers.AndXMatcher;
import com.cosmin.webserver.http.controller.routing.matchers.MethodMatcher;
import com.cosmin.webserver.http.controller.routing.matchers.PathMatcher;
import com.cosmin.webserver.http.controller.routing.matchers.RegexPathMatcher;
import com.cosmin.webserver.http.protocol.Method;

public class FileRoutes {

    public static Route getRoute(FilesService filesService) {
        return new DefaultRoute(
                AndXMatcher.of(new RegexPathMatcher("^\\/files\\/(.*).(.*)"), new MethodMatcher(Method.GET)),
                new GetFile(filesService)
        );
    }

    public static Route createRoute(FilesService filesService) {
        return new DefaultRoute(
            AndXMatcher.of(new PathMatcher("/files"), new MethodMatcher(Method.POST)),
            new CreateFile(filesService)
        );
    }

    public static Route updateRoute(FilesService filesService) {
        return new DefaultRoute(
            AndXMatcher.of(new RegexPathMatcher("^\\/files\\/(.*).(.*)"), new MethodMatcher(Method.PUT)),
            new UpdateFile(filesService)
        );
    }

    public static Route deleteRoute(FilesService filesService) {
        return new DefaultRoute(
            AndXMatcher.of(new RegexPathMatcher("^\\/files\\/(.*).(.*)"), new MethodMatcher(Method.DELETE)),
            new DeleteFile(filesService)
        );
    }
}
