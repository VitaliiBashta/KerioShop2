package logic.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.Utils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static logic.SendMethod.POST;
import static logic.Utils.sendRequest;

public class InvoiceInfoHandler implements HttpHandler {
    private Integer personId;
    private String personEmail;
    private String offerName;
    private String code;
    private Integer companyId;
    private String description;

    @Override
    public void handle(HttpExchange he) {
        String offerId = he.getRequestURI().getQuery();
        syncAndWinOffer(offerId);
        setData(offerId);
        setPersonEmail(personId);

        Utils.writeResponse(he, getEmail());
    }

    private void syncAndWinOffer(String offerId) {
        String json = "{ \"offerStatus\": 50}";
        HttpEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);

        sendRequest(Utils.RAYNET_URL + "/offer/" + offerId + "/sync", POST, null);

        sendRequest(Utils.RAYNET_URL + "/offer/" + offerId, POST, entity);
    }

    private void setData(String offerId) {
        String response = sendRequest(Utils.RAYNET_URL + "/offer/" + offerId);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        JsonOffer jsonOffer = gson.fromJson(response, JsonOffer.class);
        personId = jsonOffer.data.person.id;
        offerName = jsonOffer.data.name;
        code = jsonOffer.data.code;
        companyId = Integer.valueOf(jsonOffer.data.company.id);
        description = "";
        if (jsonOffer.data.description != null)
            description = jsonOffer.data.description
                    .replaceAll("<.*?>", "")
                    .replaceAll("&#xa0;", "");
    }

    private void setPersonEmail(Integer personId) {
        String response = sendRequest(Utils.RAYNET_URL + "/person/" + personId);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        JsonPerson jsonPerson = gson.fromJson(response, JsonPerson.class);
        personEmail = jsonPerson.data.contactInfo.email;
    }

    private String getEmail() {
        CompanyHandler.Company company = CompanyHandler.getCompany(companyId);
        String result = "mailto:simona.kotalova@zebra.cz" +
                "?" + "cc=lucie.sedlackova@zebra.cz" +
                "&subject=fakturace pro " + company.name;
        String body = "prosím o vystavení DD na \nprodejce "
                + company.name + "\nIČ:" + company.regNumber +
                "\nprodukt: " + offerName + "\nkontaktní email: " + personEmail + "\n\n" + code +
                "\n\npoznamka: \n" + description;
        String htmlBody = "";
        try {
            htmlBody = URLEncoder.encode(body, "UTF-8")
                    .replaceAll("\\+", "%20")
                    .replaceAll("%21", "!")
                    .replaceAll("%27", "'")
                    .replaceAll("%28", "(")
                    .replaceAll("%29", ")")
                    .replaceAll("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        result = result + "&body=" + htmlBody;
        return result;
    }

    private class JsonPerson {
        private Person data;

        private class Person {
            private int id;
            private String firstName;
            private String lastName;
            private ContactInfo contactInfo;

            private class ContactInfo {
                private String email;
            }
        }
    }

    private class JsonOffer {
        private OfferRead data;

        private class OfferRead {
            private Person person;
            private int id;
            private String name;
            private String code;
            private String description;
            private Company company;
        }

        private class Person {
            private Integer id;
        }
    }

    private class Company {
        String id;
        String name;
    }
}
