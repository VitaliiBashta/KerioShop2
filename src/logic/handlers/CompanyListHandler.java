package logic.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.Raynet;

import java.io.IOException;
import java.io.OutputStream;

public class CompanyListHandler implements HttpHandler {
    private final Raynet raynet;

    public CompanyListHandler(Raynet raynet) {
        this.raynet = raynet;
    }

    @Override
    public void handle(HttpExchange he) throws IOException {
        OutputStream os = he.getResponseBody();

        if (he.getRequestMethod().equals("GET")) {
            String response = raynet.companiesToJson(raynet.companies.values());
            if (!response.equals("")) {
                he.sendResponseHeaders(200, response.getBytes().length);
                os.write(response.getBytes());
            }
            os.flush();
        }
        os.close();
    }

}