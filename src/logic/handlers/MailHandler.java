package logic.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.Utils;

import static logic.Utils.sendRequest;

public class MailHandler implements HttpHandler {
    private Integer personId;
    private String personEmail;
    private String offerName;
    private String code;

    @Override
    public void handle(HttpExchange he) {
        String offerId = he.getRequestURI().getQuery();
        setPersonId(offerId);
        setPersonEmail(personId);

        Utils.writeResponse(he, getEmail());
    }

    private void setPersonId(String offerId) {
        String response = sendRequest(Utils.RAYNET_URL + "/offer/" + offerId);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        JsonOffer jsonOffer = gson.fromJson(response, JsonOffer.class);
        personId = jsonOffer.data.person.id;
        offerName = jsonOffer.data.name;
        code = jsonOffer.data.code;
    }

    private void setPersonEmail(Integer personId) {
        String response = sendRequest(Utils.RAYNET_URL + "/person/" + personId);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        JsonPerson jsonPerson = gson.fromJson(response, JsonPerson.class);
        personEmail = jsonPerson.data.contactInfo.email;

    }

    private String getEmail() {
        return "mailto:" + personEmail +
                "?" + "bcc=zebra@asistentka.raynet.cz" +
                "&subject=" + code + " " + offerName;
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
        }

        private class Person {
            private Integer id;
        }
    }
}