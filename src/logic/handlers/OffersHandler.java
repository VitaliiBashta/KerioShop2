package logic.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.Utils;

import java.util.List;

import static logic.Utils.sendRequest;

public class OffersHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) {
        String businessCaseId = he.getRequestURI().getQuery();
        Utils.writeResponse(he, getOffers(businessCaseId));
    }

    private String getOffers(String businessCaseId) {
        String response = sendRequest(Utils.RAYNET_URL + "/offer/?businessCase[EQ]=" + businessCaseId);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        JsonOffers jsonOffers = gson.fromJson(response, JsonOffers.class);
        return jsonOffers.asHTML();
    }

    private class JsonOffers {
        public boolean success;
        public int totalCount;
        private List<OfferRead> data;

        private String asHTML() {
            StringBuilder result = new StringBuilder();
            for (OfferRead aData : this.data) {
                result.append("<option value=\"")
                        .append(aData.id).append("\">")
                        .append(aData.code).append(":\t")
                        .append(aData.name)
                        .append("</option>");
            }
            return result.toString();
        }

        private class OfferRead {
            private int id;
            private String name;
            private String code;
        }
    }

}