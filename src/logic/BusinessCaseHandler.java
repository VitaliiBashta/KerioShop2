package logic;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.objects.Company;
import logic.objects.FormObject;

import java.io.*;
import java.net.URLDecoder;

class BusinessCaseHandler implements HttpHandler {
private Raynet raynet;

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

            response = businessCaseId+"";
            raynet.initBusinessCases(formObject.company);
            raynet.initOffers(formObject.company);
            Company company = raynet.getCompanyById(formObject.company);
            if (company != null) {
                company.businessCases.clear();
                company.businessCases.add(raynet.businessCases.get(businessCaseId));
                raynet.businessCases.get(businessCaseId).offer = raynet.offers.get(offerId);
            }
            he.sendResponseHeaders(200, response.getBytes().length);
            os.write(response.getBytes());
            os.flush();
        }
        os.close();
    }


}