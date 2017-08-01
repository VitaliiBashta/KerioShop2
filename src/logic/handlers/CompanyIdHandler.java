package logic.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.Utils;
import logic.objects.Company;
import webServer.WebServer;

public class CompanyIdHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) {
        if (he.getRequestMethod().equals("GET")) {
            String companyName = he.getRequestURI().getQuery();
            if (companyName != null) {
                String id = "";
                for (Company comp : WebServer.companies) {
                    if (comp.value.equals(companyName)) {
                        id = comp.data;
                        break;
                    }
                }
                String response = id;
                Utils.writeResponse(he, response);
            }
        }
    }
}