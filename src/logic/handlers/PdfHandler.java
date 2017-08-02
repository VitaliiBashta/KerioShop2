package logic.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static logic.Utils.sendRequest;

public class PdfHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) {
        String request = Utils.RAYNET_URL + "/offer/" + he.getRequestURI().getQuery() + "/pdfExport";
        String response = sendRequest(request);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonOfferPdfExport json = gson.fromJson(response, JsonOfferPdfExport.class);
        Utils.writeResponse(he, json.getDownloadUrl());
    }

    private class JsonOfferPdfExport {
        private String uuid;
        private String fileName;
        private String contentType;
        private String accessToken;
        private String instanceName;

        private String getDownloadUrl() {
            try {
                return Utils.RAYNET_URL + "/exportBody/" +
                        uuid + '/' +
                        accessToken + '/' +
                        instanceName + '/' +
                        "?fileName=\"" + URLDecoder.decode(fileName, "UTF-8") +
                        "\"&contentType=" + URLDecoder.decode(contentType, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return "";
        }
    }
}