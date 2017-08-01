package logic.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.Utils;
import webServer.WebServer;

public class CompanyListHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) {
        if (he.getRequestMethod().equals("GET")) {
            String response = Utils.objectToJson(WebServer.companies);
            System.out.println(response);
            Utils.writeResponse(he, response);
        }
    }
}