package logic.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.Raynet;
import logic.objects.FormObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLDecoder;

public class BusinessCaseHandler implements HttpHandler {
    private final Raynet raynet;

    public BusinessCaseHandler(Raynet raynet) {
        this.raynet = raynet;
    }

    @Override
    public void handle(HttpExchange he) throws IOException {
        OutputStream os = he.getResponseBody();
        if (he.getRequestMethod().equals("PUT")) {
            BufferedReader br = new BufferedReader(new InputStreamReader(he.getRequestBody(), "utf-8"));
            String query = br.readLine();
            String response;

            FormObject formObject = new FormObject(URLDecoder.decode(query, "UTF-8"));
            Integer businessCaseId = formObject.getBusinessCase().createBusinessCaseInRaynet();
            formObject.getProduct().setBusinessCaseId(businessCaseId);
            Integer productId = formObject.getProduct().createProductInRaynet();
            formObject.getOffer().setBusinessCase(businessCaseId);
            Integer offerId = formObject.getOffer().createOfferInRaynet();
            formObject.getOffer().sync();

            response = raynet.getBusinessCasesAndOffers(formObject.companyName);
            he.sendResponseHeaders(200, response.getBytes().length);
            os.write(response.getBytes());
            os.flush();
        }
        os.close();
    }
}