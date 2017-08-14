package logic.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class StaticHandler implements HttpHandler {
    static void sendFile(HttpExchange he, File file) {
        if (file.isDirectory()) {
            file = new File(file.getPath(), "index.html");
        }
        try (OutputStream output = he.getResponseBody(); FileInputStream fs = new FileInputStream(file)) {
            he.sendResponseHeaders(200, file.length());
            final byte[] buffer = new byte[1024];
            int count;
            while ((count = fs.read(buffer)) >= 0) output.write(buffer, 0, count);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(HttpExchange he) {
        String path = he.getRequestURI().getPath();
        File file = new File("wwwRoot", path);
        if (path.endsWith(".css"))
            he.getResponseHeaders().set("Content-Type", "text/css");
        sendFile(he, file);
    }
}
