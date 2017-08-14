package logic.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;

public class KerioHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange he) {
        String path = he.getRequestURI().getPath();
        File file = new File("wwwRoot/kerio", "index.html");
        StaticHandler.sendFile(he, file);
    }
}
