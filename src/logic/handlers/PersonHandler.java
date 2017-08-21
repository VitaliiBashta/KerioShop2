package logic.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.Utils;

import java.util.List;

import static logic.Utils.sendRequest;


public class PersonHandler implements HttpHandler {

    public static String getPersons(Integer companyId) {
        String response = sendRequest(Utils.RAYNET_URL + "/person/?primaryRelationship-company-id=" + companyId);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        JsonPerson jsonPerson = gson.fromJson(response, JsonPerson.class);
        return jsonPerson.asHTML();
    }

    @Override
    public void handle(HttpExchange he) {
        Integer companyId = Integer.valueOf(he.getRequestURI().getQuery());
        String response = getPersons(companyId);
        Utils.writeResponse(he, response);
    }

    private class JsonPerson {
        private List<Person> data;

        private String asHTML() {
            StringBuilder result = new StringBuilder();
            for (Person aData : data) {
                result.append("<option value=\"").append(aData.id).append("\">")
                        .append(aData.firstName).append(" ").append(aData.lastName)
                        .append("</option>");
            }
            return result.toString();
        }

        private class Person {
            private int id;
            private String firstName;
            private String lastName;
        }
    }
}