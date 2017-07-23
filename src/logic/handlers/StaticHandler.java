package logic.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class StaticHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange he) throws IOException {
        String path = he.getRequestURI().getPath();
        if (path.equals("/")) path = "index.html";
        File file = new File(new File("wwwRoot"), path);
        if (path.endsWith(".css"))
            he.getResponseHeaders().set("Content-Type", "text/css");
        he.sendResponseHeaders(200, file.length());
        OutputStream output = he.getResponseBody();
        FileInputStream fs = new FileInputStream(file);
        final byte[] buffer = new byte[1024];
        int count;
        while ((count = fs.read(buffer)) >= 0) output.write(buffer, 0, count);
        output.flush();
        output.close();
        fs.close();
    }
}
