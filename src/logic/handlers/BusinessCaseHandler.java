package logic.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.Utils;
import logic.objects.FormObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import static logic.Utils.sendRequest;

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
                Integer businessCaseId = formObject.createBusinessCaseInRaynet();

                if (formObject.offersSeparate) {
                    for (int i = 0; i < formObject.products.size(); i++) {
                        Integer offerId = formObject.createOfferInRaynet(businessCaseId, i);
                        Integer productId = formObject.createProductInRaynet(offerId, i);
                    }
                } else {
                    Integer offerId = formObject.createOfferInRaynet(businessCaseId, 0);
                    for (int i = 0; i < formObject.products.size(); i++) {
                        Integer productId = formObject.createProductInRaynet(offerId, i);
                    }
                }
                response = getBusinessCases(formObject.companyId);
                formObject.syncOffer();
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
        String response = sendRequest(Utils.RAYNET_URL + "/businessCase/?company[EQ]=" + companyId);
        JsonBusinessCase jsonBusinessCase = gson.fromJson(response, JsonBusinessCase.class);
        return jsonBusinessCase.asHTML();
    }

    public class JsonBusinessCase {
        private List<BusinessCase> data;

        String asHTML() {
            StringBuilder result = new StringBuilder();
            result.append("<option value=\"0\">(new)</option>");
            Collections.reverse(data);
            for (BusinessCase aData : data) {
                result.append("<option value=\"").append(aData.id).append("\">")
                        .append(aData.code).append(":\t")
                        .append(aData.name).append("</option>");
            }
            return result.toString();
        }

        private class BusinessCase {
            public int id;
            private String name;
            private String code;
        }
    }
}