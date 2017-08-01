package logic.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.Raynet;
import logic.objects.Company;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

public class CompanyListHandler implements HttpHandler {
    private final Raynet raynet;

    public CompanyListHandler(Raynet raynet) {
        this.raynet = raynet;
    }

    @Override
    public void handle(HttpExchange he) throws IOException {
        OutputStream os = he.getResponseBody();

        if (he.getRequestMethod().equals("GET")) {
            String response = companiesToJson(raynet.companies.values());
            if (!response.equals("")) {
                he.sendResponseHeaders(200, response.getBytes().length);
                os.write(response.getBytes());
            }
            os.flush();
        }
        os.close();
    }

    public String companiesToJson(Collection<Company> list) {
        StringBuilder result = new StringBuilder();
        for (Company item : list) {
            result.append(item.toString()).append(",");
        }
        result.insert(0, "[");
        result.deleteCharAt(result.lastIndexOf(","));
        result.append("]");
        return result.toString();
    }
}