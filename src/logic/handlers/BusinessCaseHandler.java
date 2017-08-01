package logic.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.Raynet;
import logic.objects.FormObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

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
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            FormObject formObject = gson.fromJson(query, FormObject.class);
            formObject.init();
//            FormObject formObject = new FormObject(URLDecoder.decode(query, "UTF-8"));
            Integer businessCaseId = formObject.getBusinessCase().createBusinessCaseInRaynet();


            if (formObject.offersSeparate) {
                for (int i = 0; i < formObject.products.size(); i++) {
                    Integer offerId = formObject.getOffer(i).createOfferInRaynet(businessCaseId);
                    Integer productId = formObject.getProduct(i).createProductInRaynet(offerId);
                }
            } else {
                Integer offerId = formObject.getOffer(0).createOfferInRaynet(businessCaseId);
                for (int i = 0; i < formObject.products.size(); i++) {
                    Integer productId = formObject.getProduct(i).createProductInRaynet(offerId);
                }
            }
            formObject.getOffer(0).sync();

            response = raynet.getBusinessCases(formObject.companyName);
            he.sendResponseHeaders(200, response.getBytes().length);
            os.write(response.getBytes());
            os.flush();
        }
        os.close();
    }
}