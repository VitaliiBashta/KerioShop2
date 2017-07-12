package logic;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.objects.FormObject;

import java.io.*;
import java.net.URLDecoder;

class BusinessCaseHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {
        OutputStream os = he.getResponseBody();
        if (he.getRequestMethod().equals("PUT")) {
            BufferedReader br = new BufferedReader(new InputStreamReader(he.getRequestBody(), "utf-8"));
            String query = br.readLine();
            String response;

            FormObject formObject = new FormObject(URLDecoder.decode(query, "UTF-8"));
            Integer businessCaseId =  formObject.getBusinessCase().createBusinessCaseInRaynet();

            Integer productId = formObject.getProduct().createProductInRaynet();
            Integer offerId = formObject.getOffer().createOfferInRaynet();
            formObject.getOffer().sync();

            response = formObject.getOffer().getPdfUrl();

            he.sendResponseHeaders(200, response.getBytes().length);
            os.write(response.getBytes());
            os.flush();
        }
        os.close();
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String query = "company=598&person=695&name=testtest&currency=15&owner=462&validFrom=2017-07-11&scheduledEnd=2017-07-30&businessCasePhase=42&probability=50&source=61&category=225&description=+&product=Kerio+Connect%C2%A0%2C+Kerio+Antivirus%2C+ActiveSync%2C+AntiSpam%2C+%2B1year+SWM%2C+15+users&price=19625&discountPercent=15&totalPrice=16681.25&licence=1";

        System.out.println("before:" + query);
        System.out.println("after;" + URLDecoder.decode(query, "UTF-8"));
    }
}