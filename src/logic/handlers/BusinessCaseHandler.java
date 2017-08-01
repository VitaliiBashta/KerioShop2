package logic.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.Methods;
import logic.Utils;
import logic.jsonObjects.JsonBusinessCase;
import logic.objects.FormObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BusinessCaseHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) {
        String response = "";
        if (he.getRequestMethod().equals("PUT")) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(he.getRequestBody(), "utf-8"))) {
                String query = br.readLine();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                FormObject formObject = gson.fromJson(query, FormObject.class);
                formObject.init();
                Integer businessCaseId = formObject.businessCase.createBusinessCaseInRaynet();

                if (formObject.offersSeparate) {
                    for (int i = 0; i < formObject.products.size(); i++) {
                        Integer offerId = formObject.offers.get(i).createOfferInRaynet(businessCaseId);
                        Integer productId = formObject.products.get(i).createProductInRaynet(offerId);
                    }
                } else {
                    Integer offerId = formObject.offers.get(0).createOfferInRaynet(businessCaseId);
                    for (int i = 0; i < formObject.products.size(); i++) {
                        Integer productId = formObject.products.get(i).createProductInRaynet(offerId);
                    }
                }
                response = getBusinessCases(formObject.company);
                formObject.offers.get(0).sync();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if (he.getRequestMethod().equals("GET")) {
            Integer companyId = Integer.valueOf(he.getRequestURI().getQuery());
            response = getBusinessCases(companyId);
        }
        Utils.writeResponse(he, response);
    }

    private String getBusinessCases(Integer companyId) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String response = Methods.sendGet(Utils.RAYNET_API_URL + "/businessCase/?company[EQ]=" + companyId);
        JsonBusinessCase jsonBusinessCase = gson.fromJson(response, JsonBusinessCase.class);
        return jsonBusinessCase.asHTML();
    }
}