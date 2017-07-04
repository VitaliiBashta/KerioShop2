package logic;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

class StaticHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange he) throws IOException {
        String fileId = he.getRequestURI().getPath();
        File file = new File(new File("wwwRoot"), fileId);
        OutputStream output = he.getResponseBody();
        he.sendResponseHeaders(200, file.length());

        FileInputStream fs = new FileInputStream(file);
        final byte[] buffer = new byte[1024];
        int count;
        while ((count = fs.read(buffer)) >= 0)
            output.write(buffer, 0, count);
        fs.close();
        output.close();
    }
}
