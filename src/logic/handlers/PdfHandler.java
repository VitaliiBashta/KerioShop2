package logic.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.Methods;
import logic.Utils;
import logic.jsonObjects.JsonOfferPdfExport;

public class PdfHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) {
        Integer offerId = Integer.valueOf(he.getRequestURI().getQuery());
        if (he.getRequestMethod().equals("GET")) {
            String response = getPdfUrl(offerId);
            Utils.writeResponse(he, response);
        }
    }

    private String getPdfUrl(Integer offerId) {
        String request = Utils.RAYNET_API_URL + "/offer/" + offerId + "/pdfExport";
        String response = Methods.sendGet(request);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonOfferPdfExport json = gson.fromJson(response, JsonOfferPdfExport.class);
        return json.getRequest();
    }
}