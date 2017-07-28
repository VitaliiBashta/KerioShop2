package logic.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.Raynet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;

public class DynamicHandler implements HttpHandler {
    private final Raynet raynet;

    public DynamicHandler(Raynet raynet) {
        this.raynet = raynet;
    }

    @Override
    public void handle(HttpExchange he) throws IOException {
        URI requestedUri = he.getRequestURI();

        OutputStream os = he.getResponseBody();

        if (he.getRequestMethod().equals("GET")) {
            String query = requestedUri.getQuery();
            String response = "";
            if (query != null) {
                System.out.println(">>> GET parameters:\t" + query);
                if (query.startsWith("getPersonsFor")) {
                    String companyName = query.split("=")[1];
                    response = raynet.getPersons(companyName);
                }
                if (query.startsWith("getIdFor")) {
                    String companyName = query.split("=")[1];
                    System.out.println("response for getIdFor (" + companyName + ")=" + raynet.companies.get(companyName).id);
                    response = String.valueOf(raynet.companies.get(companyName).id);
                }
                if (query.startsWith("getBusinessCasesFor")) {
                    String companyName = query.split("=")[1];
                    response = raynet.getBusinessCases(companyName);
                }
                if (query.startsWith("getOffers")) {
                    Integer businessCaseId = Integer.valueOf(query.split("=")[1]);
                    response = raynet.getOffers(businessCaseId);
                }

                if (query.startsWith("getPdfUrl")) {
                    Integer offerId = Integer.valueOf(query.split("=")[1]);
                    response = raynet.getPdfUrl(offerId);
                }
            }
            if (!response.equals("")) {
                System.out.println("<<<: \t" + response);
                he.sendResponseHeaders(200, response.getBytes().length);
                os.write(response.getBytes());
            }
            os.flush();
        }

        if (he.getRequestMethod().equals("POST")) {
            BufferedReader br = new BufferedReader(new InputStreamReader(he.getRequestBody(), "utf-8"));
            String query = br.readLine();
            System.out.println("??? Getting unknown POST query: " + query);
            String response = "got it";
            he.sendResponseHeaders(200, response.length());
            os.write(response.getBytes());
            os.flush();
        }
        os.close();
    }

}