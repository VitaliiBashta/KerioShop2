package logic.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.Methods;
import logic.Utils;
import logic.jsonObjects.JsonOffer;

public class OffersHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) {
        String query = he.getRequestURI().getQuery();
        if (he.getRequestMethod().equals("GET")) {
            Integer businessCaseId = Integer.valueOf(query);
            String response = getOffers(businessCaseId);
            Utils.writeResponse(he, response);
        }
    }

    private String getOffers(Integer businessCaseId) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String response = Methods.sendGet(Utils.RAYNET_API_URL + "/offer/?businessCase[EQ]=" + businessCaseId);
        JsonOffer jsonOffer = gson.fromJson(response, JsonOffer.class);
        return jsonOffer.asHTML();
    }
}