package logic;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.objects.FormObject;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import webAccess.Methods;

import java.io.*;

class BusinessCaseHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        OutputStream os = he.getResponseBody();
        if (he.getRequestMethod().equals("PUT")) {
            BufferedReader br = new BufferedReader(new InputStreamReader(he.getRequestBody(), "utf-8"));
            String query = br.readLine();
            String response;

            FormObject formObject = new FormObject(query);
            Integer businessCaseId =  formObject.getBusinessCase().createBusinessCaseInRaynet();
            Integer offerId = formObject.getOffer().createOfferInRaynet();

            if (formObject.getOffer().sync())
                response = "Created BusinessCase:" + businessCaseId + " and offer:" + offerId;
            else response = "Something went wrong";
            he.sendResponseHeaders(200, response.getBytes().length);
            os.write(response.getBytes());
            os.flush();
        }
        os.close();
    }
}